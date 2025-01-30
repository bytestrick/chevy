package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.control.Interaction;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Coin;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.Key;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;

public final class CollectableController {
    private final CoinController coinController;
    private final HealthController healthController;
    private final KeyController keyController;
    private final PowerUpController powerUpController;

    public CollectableController(Chamber chamber, HUDController hudController, EnemyUpdateController enemyUpdateController, ProjectileUpdateController projectileUpdateController) {
        keyController = new KeyController(chamber, hudController);
        healthController = new HealthController(hudController, chamber);
        coinController = new CoinController(chamber, hudController);
        powerUpController = new PowerUpController(chamber, hudController, enemyUpdateController, projectileUpdateController);
    }

    /**
     * Manages the received interaction
     *
     * @param interaction kind of interaction to manage
     * @param subject     entity author of the interaction (subject)
     * @param object      entity receiver of the interaction (object)
     */
    public void handleInteraction(Interaction interaction, Entity subject, Collectable object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, object);
            case UPDATE -> update(subject);
            default -> {
            }
        }
    }

    /**
     * Manages the interaction of the {@link Player} with a collectable item
     *
     * @param collectable item to collect
     */
    private void playerInInteraction(Player player, Collectable collectable) {
        switch (collectable.getType()) {
            case Collectable.Type.HEALTH -> healthController.playerInInteraction(player, (Health) collectable);
            case Collectable.Type.COIN -> coinController.playerInInteraction((Coin) collectable);
            case Collectable.Type.KEY -> keyController.playerInInteraction((Key) collectable);
            default -> {
            }
        }
        if (collectable.getGenericType() == Collectable.Type.POWER_UP) {
            powerUpController.playerInInteraction(player, (PowerUp) collectable);
        }
    }

    private void update(Entity subject) {
        switch (subject.getType()) {
            case Collectable.Type.HEALTH -> HealthController.update((Health) subject);
            case Collectable.Type.COIN -> CoinController.update((Coin) subject);
            case Collectable.Type.KEY -> KeyController.update((Key) subject);
            default -> {
            }
        }
        if (subject.getGenericType() == Collectable.Type.POWER_UP) {
            assert subject instanceof PowerUp;
            powerUpController.update((PowerUp) subject);
        }
    }
}
