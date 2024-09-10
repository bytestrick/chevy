package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
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
     * @param v      la trappola Void con cui il giocatore interagisce
     */
    void playerInInteraction(Void v) {
        playerController.handleInteraction(Interaction.TRAP, v);
    }
}
