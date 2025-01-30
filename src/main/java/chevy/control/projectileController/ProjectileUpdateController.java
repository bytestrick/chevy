package chevy.control.projectileController;

import chevy.control.Interaction;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages the addition, removal, and update of the projectiles
 */
public final class ProjectileUpdateController implements Updatable {
    private final boolean running;
    /**
     * Projectile controller to manage the specific updates of the projectiles
     */
    private final ProjectileController projectileController;
    /**
     * Projectiles currently in the game
     */
    private final List<Projectile> projectiles;
    /**
     * Temporary list of projectiles to add to the main list
     */
    private final List<Projectile> projectilesToAdd;
    private boolean paused;

    /**
     * @param projectileController projectile controller to manage the specific updates of the projectiles
     * @param projectiles          projectiles to add
     */
    public ProjectileUpdateController(ProjectileController projectileController,
                                      List<Projectile> projectiles) {
        this.projectileController = projectileController;

        // We use LinkedList for efficient removal
        this.projectiles = new LinkedList<>();
        projectilesToAdd = projectiles;
        running = true;
        paused = false;

        // Add this controller to the updates managed by UpdateManager
        UpdateManager.register(this);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Private method to add the projectiles to the main list. It is called before each update iteration. This approach avoids {@code ConcurrentModificationException} during list iteration.
     */
    private void addProjectile() {
        projectiles.addAll(projectilesToAdd);
        projectilesToAdd.clear(); // Clear the temporary list
    }

    /**
     * Update method called at each game cycle to manage the updates of the projectiles.
     */
    @Override
    public void update(double delta) {
        if (paused) {
            return;
        }
        addProjectile(); // add the projectiles to the main list

        Iterator<Projectile> it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile projectile = it.next();
            projectileController.handleInteraction(Interaction.UPDATE, projectile, null); //
            // Handles the update of the projectile
            if (projectile.isCollision()) { // if the projectile has collided remove it
                it.remove();
            }
        }
    }

    @Override
    public boolean updateFinished() {
        return !running;
    }
}
