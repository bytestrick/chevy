package chevy.control.collectableController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Coin;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.Key;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;


public class CollectableController {
    private final PlayerController playerController;
    private final CoinController coinController;
    private final HealthController healthController;
    private final KeyController keyController;
    private final PowerUpController powerUpController;
    private final Chamber chamber;


    public CollectableController(Chamber chamber, PlayerController playerController, PowerUpTextVisualizerController powerUpTextVisualizerController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.keyController = new KeyController(chamber);
        this.healthController = new HealthController(chamber);
        this.coinController = new CoinController(chamber);
        this.powerUpController = new PowerUpController(chamber, powerUpTextVisualizerController);
    }


    /**
     * Gestisce il tipo di interazione ricevuta.
     * @param interaction il tipo di interazione da gestire.
     * @param subject l'entità che avvia l'interazione.
     * @param object l'entità che subisce l'interazione.
     */
    public void handleInteraction(InteractionType interaction, Entity subject, Collectable object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, object);
            case UPDATE ->  update(subject);
            default -> {}
        }
    }

    /**
     * Gestisce l'interazione di un giocatore con un oggetto collezionabile.
     * @param collectable l'oggetto da collezionare.
     */
    private void playerInInteraction(Player player, Collectable collectable) {
        switch (collectable.getSpecificType()) {
            case Collectable.Type.HEALTH -> healthController.playerInInteraction((Health) collectable);
            case Collectable.Type.COIN -> coinController.playerInInteraction((Coin) collectable);
            case Collectable.Type.KEY -> keyController.playerInInteraction((Key) collectable);
            default -> {}
        }
        if (collectable.getGenericType() == Collectable.Type.POWER_UP) {
            powerUpController.playerInInteraction(player, (PowerUp) collectable);
        }
    }

    private void update(Entity subject) {
        switch (subject.getSpecificType()) {
            case Collectable.Type.HEALTH -> healthController.update((Health) subject);
            case Collectable.Type.COIN -> coinController.update((Coin) subject);
            case Collectable.Type.KEY -> keyController.update((Key) subject);
            default -> {}
        }
        if (subject.getGenericType() == Collectable.Type.POWER_UP) {
            powerUpController.update((PowerUp) subject);
        }
    }
}