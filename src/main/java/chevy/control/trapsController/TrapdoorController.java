package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;

/**
 * Gestisce le interazioni del giocatore con la botola nel gioco.
 */
public final class TrapdoorController {
    /**
     * Stanza di gioco in cui si trova la botola da gestire
     */
    private final PlayerController playerController;

    TrapdoorController(PlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * Gestisce l'interazione del giocatore quando è sopra la botola.
     */
    public void playerInInteraction(Trapdoor trapdoor) {
        if (!trapdoor.isSafeToCross()) {
            playerController.handleInteraction(Interaction.TRAP, trapdoor);
        }
    }

    /**
     * Gestisce l'interazione della botola quando il giocatore esce da essa.
     *
     * @param trapdoor la botola con cui interagisce il giocatore
     */
    public void playerOutInteraction(Trapdoor trapdoor) {
        trapdoor.changeState(Trapdoor.State.OPEN);
    }

    public void update(Trapdoor trapdoor) {
        if (trapdoor.checkAndChangeState(Trapdoor.State.DAMAGE)) {
            trapdoor.activated();
        }
    }
}
