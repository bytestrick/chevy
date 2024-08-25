package chevy.control.projectileController;

import chevy.control.InteractionType;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.service.Update;
import chevy.service.UpdateManager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Gestisce gli aggiornamenti dei proiettili nel gioco.
 * Gestisce l'aggiunta, la rimozione e l'aggiornamento dei proiettili.
 */
public class ProjectileUpdateController implements Update {
    private static boolean STOP_UPDATE = false;
    /**
     * Controller dei proiettili per gestire gli aggiornamenti specifici dei proiettili.
     */
    private final ProjectileController projectileController;
    /**
     * Lista dei proiettili attualmente presenti nel gioco.
     */
    private final List<Projectile> projectiles;
    /**
     * Lista temporanea dei proiettili da aggiungere alla lista principale.
     */
    private final List<Projectile> projectilesToAdd;
    private boolean updateFinished = false;

    /**
     * @param projectileController controller dei proiettili per gestire gli aggiornamenti dei proiettili
     * @param projectiles          lista di proiettili da aggiungere all'aggiornamento
     */
    public ProjectileUpdateController(ProjectileController projectileController, List<Projectile> projectiles) {
        this.projectileController = projectileController;
        this.projectiles = new LinkedList<>(); // Utilizziamo LinkedList per una rimozione efficiente
        this.projectilesToAdd = projectiles;

        UpdateManager.addToUpdate(this); // Aggiungiamo questo controller agli aggiornamenti gestiti da UpdateManager
    }

    /**
     * Metodo privato per aggiungere i proiettili alla lista principale. Viene chiamato prima di ogni iterazione di
     * aggiornamento.
     * Questo approccio evita ConcurrentModificationException durante l'iterazione della lista.
     */
    private void addProjectile() {
        this.projectiles.addAll(projectilesToAdd);
        projectilesToAdd.clear(); // Pulisce la lista temporanea dopo aver aggiunto i proiettili
    }

    public static void stopUpdate() {
        STOP_UPDATE = true;
    }

    public static void runUpdate() {
        STOP_UPDATE = false;
    }

    /**
     * Metodo di aggiornamento chiamato a ogni ciclo di gioco per gestire gli aggiornamenti dei proiettili.
     *
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void update(double delta) {
        if (STOP_UPDATE) return;

        addProjectile(); // Aggiunge i proiettili alla lista principale prima dell'iterazione

        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile projectile = it.next();
            projectileController.handleInteraction(InteractionType.UPDATE, projectile, null); // Gestisce
            // l'aggiornamento del proiettile
            if (projectile.isCollision()) { // Se il proiettile collide, si rimuove dalla lista
                it.remove();
            }
        }
    }

    public void updateTerminate() {
        updateFinished = true;
    }

    @Override
    public boolean updateFinished() {
        return updateFinished;
    }
}