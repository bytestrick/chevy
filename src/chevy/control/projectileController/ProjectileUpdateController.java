package chevy.control.projectileController;

import chevy.control.trapsController.TrapsController;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Traps;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.List;

public class ProjectileUpdateController implements Update {
    private final ProjectileController projectileController;
    private final List<Projectile> projectiles;

    public ProjectileUpdateController(ProjectileController projectileController, List<Projectile> projectiles) {
        this.projectileController = projectileController;
        this.projectiles = projectiles;

        UpdateManager.addToUpdate(this);
    }

    @Override
    public void update(double delta) {
        for (Projectile projectile : projectiles) {
            projectile.incrementNUpdate();
            if (projectile.getUpdateEverySecond() * GameSettings.FPS == projectile.getCurrentNUpdate()) {
                projectile.resetNUpdate();
                projectileController.updateProjectile(projectile);
            }
        }
    }

    @Override
    public boolean isEnd() {
        return false;
    }
}
