package chevy.control.trapsController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;

/**
 * Gestisce le interazioni del giocatore con la botola nel gioco.
 */
public class TrapdoorController {
    /**
     * Stanza di gioco in cui si trova la botola da gestire
     */
    private final Chamber chamber;
    private final PlayerController playerController;

    public TrapdoorController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce l'interazione del giocatore quando è sopra la botola.
     */
    public void playerInInteraction(Trapdoor trapdoor) {
        if (!trapdoor.isSafeToCross()) {
            playerController.handleInteraction(InteractionType.TRAP, trapdoor);
        }
    }

    /**
     * Gestisce l'interazione della botola quando il giocatore esce da essa.
     *
     * @param trapdoor la botola con cui interagisce il giocatore
     */
    public void playerOutInteraction(Trapdoor trapdoor) {
        trapdoor.changeState(Trapdoor.EnumState.OPEN);
    }

    public void update(Trapdoor trapdoor) {
        if (trapdoor.checkAndChangeState(Trapdoor.EnumState.DAMAGE)) {
            trapdoor.activated();
        }
    }
}
