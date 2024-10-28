package chevy.control.projectileController;

import chevy.control.Interaction;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Gestisce l'aggiunta, la rimozione e l'aggiornamento dei proiettili
 */
public final class ProjectileUpdateController implements Updatable {
    private boolean running, paused;
    /**
     * Controller dei proiettili per gestire gli aggiornamenti specifici dei proiettili
     */
    private final ProjectileController projectileController;
    /**
     * Lista dei proiettili attualmente presenti nel gioco
     */
    private final List<Projectile> projectiles;
    /**
     * Lista temporanea dei proiettili da aggiungere alla lista principale
     */
    private final List<Projectile> projectilesToAdd;

    /**
     * @param projectileController controller dei proiettili per gestire gli aggiornamenti dei
     *                             proiettili
     * @param projectiles          lista di proiettili da aggiungere all'aggiornamento
     */
    public ProjectileUpdateController(ProjectileController projectileController,
                                      List<Projectile> projectiles) {
        this.projectileController = projectileController;

        // Utilizziamo LinkedList per una rimozione efficiente
        this.projectiles = new LinkedList<>();
        projectilesToAdd = projectiles;
        running = true;
        paused = false;

        // Aggiungiamo questo controller agli aggiornamenti gestiti da UpdateManager
        UpdateManager.register(this);
    }

    // TODO: rimuovimi
    public void stopUpdate() {running = false;}

    public void setPaused(boolean paused) {this.paused = paused;}

    /**
     * Metodo privato per aggiungere i proiettili alla lista principale. Viene chiamato prima di
     * ogni iterazione di
     * aggiornamento.
     * Questo approccio evita ConcurrentModificationException durante l'iterazione della lista.
     */
    private void addProjectile() {
        projectiles.addAll(projectilesToAdd);
        projectilesToAdd.clear(); // Pulisce la lista temporanea dopo aver aggiunto i proiettili
    }

    /**
     * Metodo di aggiornamento chiamato a ogni ciclo di gioco per gestire gli aggiornamenti dei
     * proiettili.
     */
    @Override
    public void update(double delta) {
        if (paused) {
            return;
        }
        addProjectile(); // Aggiunge i proiettili alla lista principale prima dell'iterazione

        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile projectile = it.next();
            projectileController.handleInteraction(Interaction.UPDATE, projectile, null); //
            // Gestisce l'aggiornamento del proiettile
            if (projectile.isCollision()) { // Se il proiettile collide, si rimuove dalla lista
                it.remove();
            }
        }
    }

    @Override
    public boolean updateFinished() {return !running;}
}
