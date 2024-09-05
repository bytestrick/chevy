package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;

public final class HealthController {
    private final Chamber chamber;
    private final HUDController hudController;

    public HealthController(HUDController hudController, Chamber chamber) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    public void playerInInteraction(Player player, Health health) {
        if (health.changeState(Health.State.COLLECTED)) {
            player.increaseCurrentHealth(health.getRecoverHealth());
            Sound.play(Sound.Effect.HEALTH_POTION);
            health.collect();
            Data.increment("stats.collectable.total.count");
            Data.increment("stats.collectable.commons.healthPotions.count");
            hudController.changeHealth(player.getCurrentHealth());
            chamber.findAndRemoveEntity(health);
        }
    }

    public void update(Health health) {
        if (health.isCollected()) {
            if (health.getState(Health.State.COLLECTED).isFinished()) {
                health.setToDraw(false);
                health.removeFromUpdate();
            }
        }
    }
}
