package chevy.control.projectileController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.dynamicEntity.projectile.Arrow;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.dynamicEntity.projectile.SlimeShot;

/**
 * Manages the interactions and updates of the projectiles in the game, delegating the specific management to more specific controllers like ArrowController and SlimeShotController.
 */
public final class ProjectileController {
    /**
     * Controller for managing arrows
     */
    private final ArrowController arrowController;
    /**
     * Controller for managing slime shots
     */
    private final SlimeShotController slimeShotController;

    /**
     * @param chamber          the game room
     * @param playerController the player controller
     * @param enemyController  the enemy controller
     */
    public ProjectileController(Chamber chamber, PlayerController playerController,
                                EnemyController enemyController) {
        arrowController = new ArrowController(chamber, playerController, enemyController);
        slimeShotController = new SlimeShotController(chamber, playerController, enemyController);
    }

    /**
     * Manages the interactions of the projectiles based on the type of interaction and the subjects involved.
     *
     * @param interaction the type of interaction to manage
     * @param subject     entity that starts the interaction
     * @param projectile  entity that is hit by the interaction
     */
    public void handleInteraction(Interaction interaction, DynamicEntity subject,
                                  Projectile projectile) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction(projectile);
            case UPDATE -> updateProjectile((Projectile) subject);
        }
    }

    /**
     * Manages the interaction between a player and a projectile, delegating the control to the appropriate controller based on the type of projectile.
     *
     * @param projectile projectile interacting with the player
     */
    private void playerInInteraction(Projectile projectile) {
        switch (projectile.getType()) {
            case Projectile.Type.ARROW -> arrowController.playerInInteraction((Arrow) projectile);
            case Projectile.Type.SLIME_SHOT ->
                    slimeShotController.playerInInteraction((SlimeShot) projectile);
            default -> {}
        }
    }

    /**
     * Updates the state of a projectile, delegating the control to the appropriate controller based on the type of projectile.
     *
     * @param projectile projectile to update
     */
    private synchronized void updateProjectile(Projectile projectile) {
        switch (projectile.getType()) {
            case Projectile.Type.ARROW -> arrowController.update((Arrow) projectile);
            case Projectile.Type.SLIME_SHOT -> slimeShotController.update((SlimeShot) projectile);
            default -> {}
        }
    }
}
