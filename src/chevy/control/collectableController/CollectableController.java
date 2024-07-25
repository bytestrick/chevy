package chevy.control.collectableController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Coin;
import chevy.model.entity.collectable.Collectable;


public class CollectableController {
    private final PlayerController playerController;
    private final CoinController coinController;
    private final Chamber chamber;


    public CollectableController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.coinController = new CoinController(chamber);
    }


    /**
     * Gestisce il tipo di interazione ricevuta.
     * @param interaction il tipo di interazione da gestire.
     * @param subject l'entità che avvia l'interazione.
     * @param object l'entità che subisce l'interazione.
     */
    public void handleInteraction(InteractionTypes interaction, Entity subject, Collectable object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction(object);
            case UPDATE ->  update(subject);
            default -> {}
        }
    }

    /**
     * Gestisce l'interazione di un giocatore con un oggetto collezionabile.
     * @param collectable l'oggetto da collezionare.
     */
    private void playerInInteraction(Collectable collectable) {
        switch (collectable.getSpecificType()) {
            case Collectable.Type.HEALTH -> {
                playerController.handleInteraction(InteractionTypes.COLLECTABLE, collectable);
                collectable.collect();
                chamber.findAndRemoveEntity(collectable, false);
            }
            case Collectable.Type.COIN -> coinController.playerInInteraction((Coin) collectable);
            default -> {}
        }
    }

    private void update(Entity subject) {
        switch (subject.getSpecificType()) {
            case Collectable.Type.COIN -> coinController.update((Coin) subject);
            default -> {}
        }
    }
}

