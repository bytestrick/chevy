package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.service.Sound;

/**
 * Gestisce le interazioni del giocatore con la melma nel gioco
 */
final class SludgeController {
    private final Chamber chamber;

    /**
     * @param chamber la camera di gioco in cui si trova la melma
     */
    SludgeController(Chamber chamber) {this.chamber = chamber;}

    /**
     * Gestisce l'interazione iniziale (appena entra nella melma) del giocatore con la melma.
     *
     * @param player il giocatore che interagisce con la melma
     */
    static void playerInInteraction(Player player) {
        player.changeState(Player.State.SLUDGE);
        Sound.play(Sound.Effect.MUD);
    }

    /**
     * Gestisce l'interazione continua (quando e dentro la melma) del giocatore con la melma.
     *
     * @param player il giocatore che interagisce con la melma
     * @param sludge la melma in cui il giocatore è intrappolato
     */
    void playerInteraction(Player player, Sludge sludge) {
        if (sludge.getNMoveToUnlock() <= 0) {
            player.changeState(Player.State.IDLE);
            chamber.findAndRemoveEntity(sludge, false);
        } else {
            Sound.play(Sound.Effect.MUD);
            sludge.decreaseNMoveToUnlock();
        }
    }
}
