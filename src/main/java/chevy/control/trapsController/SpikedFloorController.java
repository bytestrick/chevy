package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.collectable.powerUp.StoneBoots;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.service.Sound;

/**
 * Controller to manage the interactions of the player and entities with the spiked floor in game.
 */
final class SpikedFloorController {
    private final Chamber chamber;
    private final PlayerController playerController;
    private final EnemyController enemyController;

    /**
     * @param chamber the game room containing the spiked floor
     */
    SpikedFloorController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }

    void playerInInteraction(Player player, SpikedFloor spikedFloor) {
        if (!spikedFloor.isSafeToCross()) {
            if (canHitPlayer(player)) {
                playerController.handleInteraction(Interaction.TRAP, spikedFloor);
            }
        }
    }

    /**
     * Updates the state of the spiked floor.
     *
     * @param spikedFloor the spiked floor to update
     */
    void update(SpikedFloor spikedFloor) {
        if (spikedFloor.checkAndChangeState(SpikedFloor.State.ACTIVATED)) {
            spikedFloor.setSafeToCross(false);
        }

        if (spikedFloor.checkAndChangeState(SpikedFloor.State.DISABLED)) {
            spikedFloor.setSafeToCross(true);
        }

        if (spikedFloor.checkAndChangeState(SpikedFloor.State.DAMAGE)) {
            Sound.play(Sound.Effect.SPIKE);
            Entity entity = chamber.getEntityOnTop(spikedFloor.getPosition());
            if (entity instanceof Player player && canHitPlayer(player)) {
                playerController.handleInteraction(Interaction.TRAP, spikedFloor);
            }
            if (entity instanceof Enemy enemy) {
                enemyController.handleInteraction(Interaction.TRAP, spikedFloor, enemy);
            }
        }
    }

    private static boolean canHitPlayer(Player player) {
        StoneBoots stoneBoots = (StoneBoots) player.getOwnedPowerUp(PowerUp.Type.STONE_BOOTS);
        return stoneBoots == null || !stoneBoots.canUse();
    }
}
