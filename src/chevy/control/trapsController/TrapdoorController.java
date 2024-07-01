package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utils.Vector2;

/**
 * Controller per gestire le interazioni del giocatore con la botola nel gioco.
 */
public class TrapdoorController {
    /**
     * Stanza di gioco in cui si trova la botola da gestire
     */
    private final Chamber chamber;

    public TrapdoorController(Chamber chamber) {
        this.chamber = chamber;
    }

    /**
     * Gestisce l'interazione del giocatore quando è sopra la botola.
     * @param player il giocatore che interagisce con la botola
     */
    public void playerInInteraction(Player player) {
        player.changeState(Player.EnumState.IDLE);
    }

    /**
     * Gestisce l'interazione della botola quando il giocatore esce da essa.
     * @param trapdoor la botola con cui interagisce il giocatore
     */
    public void playerOutInteraction(Trapdoor trapdoor) {
        // Rimuove la botola e aggiunge un'entità Void al suo posto.
        chamber.removeEntityOnTop(trapdoor);
        chamber.addEntityOnTop(new Void(new Vector2<>(
                trapdoor.getRow(),
                trapdoor.getCol()
        )));
    }
}
