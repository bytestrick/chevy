package chevy.control.projectileController;

import chevy.control.InteractionTypes;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ProjectileUpdateController implements Update {
    private final ProjectileController projectileController;
    private final List<Projectile> projectiles;
    private final List<Projectile> projectilesToAdd;


    public ProjectileUpdateController(ProjectileController projectileController, List<Projectile> projectiles) {
        this.projectileController = projectileController;
        this.projectiles = new LinkedList<>();
        this.projectilesToAdd = projectiles;

        UpdateManager.addToUpdate(this);
    }


    // usato questo metodo perché l'aggiunta di elementi nella lista
    // mentre viene iterata da un eccezione, in questo modo la lista
    // da iterare viene formata prima dell'iterazione e non viene più
    // modificata fino all'iterazione successiva
    private void addProjectile() {
        this.projectiles.addAll(projectilesToAdd);
        projectilesToAdd.clear();
    }

    @Override
    public void update(double delta) {
        addProjectile();

        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile projectile = it.next();
            projectile.incrementNUpdate();
            if (projectile.getUpdateEverySecond() * GameSettings.FPS == projectile.getCurrentNUpdate()) {
                projectile.resetNUpdate();
                projectileController.handleInteraction(InteractionTypes.UPDATE, projectile, null);
            }
            if (projectile.isCollide())
                it.remove();
        }
    }
}
