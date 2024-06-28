package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.Sludge;

/**
 * Controller per gestire le interazioni del giocatore con la melma nel gioco.
 */
public class SludgeController {
    private final Chamber chamber;

    /**
     * @param chamber la camera di gioco in cui si trova la melma
     */
    public SludgeController(Chamber chamber) {
        this.chamber = chamber;
    }

    /**
     * Gestisce l'interazione iniziale (appena entra nella melma) del giocatore con la melma.
     * @param player il giocatore che interagisce con la melma
     */
    public void playerInInteraction(Player player, Sludge sludge) {
        player.changeState(Player.EnumState.SLUDGE);
    }

    /**
     * Gestisce l'interazione continua (quando e dentro la melma) del giocatore con la melma.
     * @param player il giocatore che interagisce con la melma
     * @param sludge la melma in cui il giocatore è intrappolato
     */
    public void playerInteraction(Player player, Sludge sludge) {
        if (sludge.getNMoveToUnlock() <= 0) {
            player.changeState(Player.EnumState.IDLE);
            chamber.findAndRemoveEntity(sludge);
        }
        else {
            sludge.decreaseNMoveToUnlock();
        }
    }
}
