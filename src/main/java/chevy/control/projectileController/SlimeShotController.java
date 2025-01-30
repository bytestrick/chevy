package chevy.control.projectileController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.projectile.SlimeShot;

/**
 * Handles the interactions and specific updates of the {@link SlimeShot} projectiles in the game.
 */
final class SlimeShotController {
    private final Chamber chamber;
    private final PlayerController playerController;
    private final EnemyController enemyController;

    /**
     * @param chamber          game room containing the {@link SlimeShot}
     * @param playerController player controller
     * @param enemyController  enemy controller
     */
    SlimeShotController(Chamber chamber, PlayerController playerController,
                        EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }

    /**
     * Handles the player interaction with the {@link SlimeShot}
     *
     * @param projectile Slime Shot interacting with the player
     */
    void playerInInteraction(SlimeShot projectile) {
        if (projectile.changeState(SlimeShot.State.END)) {
            chamber.findAndRemoveEntity(projectile);
            projectile.setCollision(true);
            playerController.handleInteraction(Interaction.PROJECTILE, projectile);
        }
    }

    /**
     * Handles the update of the {@link SlimeShot}
     *
     * @param projectile SlimeShot to update
     */
    void update(SlimeShot projectile) {
        if (projectile.checkAndChangeState(SlimeShot.State.LOOP)) {
            Entity nextEntity = chamber.getEntityNearOnTop(projectile, projectile.getDirection());

            switch (nextEntity.getGenericType()) {
                case LiveEntity.Type.PLAYER ->
                        playerController.handleInteraction(Interaction.PROJECTILE, projectile);
                case LiveEntity.Type.ENEMY ->
                        enemyController.handleInteraction(Interaction.PROJECTILE, projectile,
                                (Enemy) nextEntity);
                default -> {}
            }

            if (nextEntity.isCrossable()) {
                chamber.moveDynamicEntity(projectile, projectile.getDirection());
            } else if (projectile.changeState(SlimeShot.State.END)) {
                chamber.findAndRemoveEntity(projectile);
                projectile.setCollision(true);
            }
        }
    }
}
