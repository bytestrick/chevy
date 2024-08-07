package chevy.control.trapsController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;

/**
 * Controller per gestire le interazioni del giocatore e delle entità con il pavimento spinato nel gioco.
 */
public class SpikedFloorController {
    private final Chamber chamber;
    private final PlayerController playerController;
    private final EnemyController enemyController;

    /**
     * @param chamber la camera di gioco si trova il pavimento spinato
     */
    public SpikedFloorController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }

    public void playerInInteraction(SpikedFloor spikedFloor) {
        playerController.handleInteraction(InteractionType.TRAP, spikedFloor);
    }

    /**
     * Aggiorna lo stato del pavimento spinato.
     *
     * @param spikedFloor il pavimento spinato da aggiornare
     */
    public void update(SpikedFloor spikedFloor) {
        if (spikedFloor.checkAndChangeState(SpikedFloor.EnumState.ACTIVATED)) {
            spikedFloor.activated();
        }

        if (spikedFloor.checkAndChangeState(SpikedFloor.EnumState.DISABLED)) {
            spikedFloor.disabled();
        }

        if (spikedFloor.checkAndChangeState(SpikedFloor.EnumState.DAMAGE)) {
            Entity entity = chamber.getEntityOnTop(spikedFloor);
            if (entity instanceof Player) {
                playerController.handleInteraction(InteractionType.TRAP, spikedFloor);
            }
            if (entity instanceof Enemy enemy) {
                enemyController.handleInteraction(InteractionType.TRAP, spikedFloor, enemy);
            }
        }
    }
}
