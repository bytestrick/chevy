package chevy.control.projectileController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.projectile.Arrow;

/**
 * Manages the arrows (Projectile) in the game, including handling collisions and updating their positions.
 */
final class ArrowController {
    /**
     * Reference to the game room containing the arrow.
     */
    private final Chamber chamber;
    /**
     * Reference to the player controller, used to manage interactions between the arrow and the player.
     */
    private final PlayerController playerController;
    /**
     * Reference to the enemy controller, used to manage interactions between the arrow and the enemies.
     */
    private final EnemyController enemyController;

    /**
     * @param chamber          the game room
     * @param playerController the player controller
     * @param enemyController  the enemy controller
     */
    ArrowController(Chamber chamber, PlayerController playerController,
                    EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }

    /**
     * Manage the interactions of the arrow with the player.
     *
     * @param arrow the projectile (arrow) interacting with the player
     */
    void playerInInteraction(Arrow arrow) {
        if (arrow.changeState(Arrow.State.END)) {
            chamber.findAndRemoveEntity(arrow);
            arrow.setCollision(true);
            playerController.handleInteraction(Interaction.PROJECTILE, arrow);
        }
    }

    /**
     * Update the arrow's state at each game cycle, managing its position and collisions.
     *
     * @param arrow the projectile (arrow) to update
     */
    void update(Arrow arrow) {
        if (arrow.checkAndChangeState(Arrow.State.LOOP)) {
            Entity nextEntity = chamber.getEntityNearOnTop(arrow, arrow.getDirection());

            switch (nextEntity.getGenericType()) {
                case LiveEntity.Type.PLAYER ->
                        playerController.handleInteraction(Interaction.PROJECTILE, arrow);
                case LiveEntity.Type.ENEMY ->
                        enemyController.handleInteraction(Interaction.PROJECTILE, arrow,
                                (Enemy) nextEntity);
                default -> {}
            }

            if (nextEntity.isCrossable()) {
                chamber.moveDynamicEntity(arrow, arrow.getDirection());
            } else if (arrow.changeState(Arrow.State.END)) {
                chamber.findAndRemoveEntity(arrow);
                arrow.setCollision(true);
            }
        }
    }
}
