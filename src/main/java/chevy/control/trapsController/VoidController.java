package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.Void;

/**
 * Controller per gestire l'interazione del giocatore con la trappola Void.
 */
final class VoidController {
    /**
     * Controller del giocatore per gestire l'interazioni con il giocatore
     */
    private final PlayerController playerController;

    VoidController(PlayerController playerController) {this.playerController = playerController;}

    /**
     * Gestisce l'interazione del giocatore con la trappola Void.
     *
     * @param player il giocatore che interagisce con la trappola
     * @param v      la trappola Void con cui il giocatore interagisce
     */
    void playerInInteraction(Player player, Void v) {
        if (player.changeState(Player.State.FALL)) {
            playerController.handleInteraction(Interaction.TRAP, v);
        }
    }
}
