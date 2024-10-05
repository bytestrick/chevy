package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.service.Data;
import chevy.service.Sound;

final class HealthController {
    private final Chamber chamber;
    private final HUDController hudController;

    HealthController(HUDController hudController, Chamber chamber) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    static void update(Health health) {
        if (health.isCollected()) {
            if (health.getState(Health.State.COLLECTED).isFinished()) {
                health.setShouldDraw(false);
                health.removeFromUpdate();
            }
        }
    }

    void playerInInteraction(Player player, Health health) {
        if (health.changeState(Health.State.COLLECTED)) {
            player.increaseCurrentHealth(health.getRecoverHealth());
            Sound.play(Sound.Effect.HEALTH_POTION);
            health.collect();
            Data.increment("stats.collectable.totalCollected.count");
            Data.increment("stats.collectable.commons.healthPotions.count");
            hudController.changeHealth(player.getCurrentHealth());
            chamber.findAndRemoveEntity(health);
        }
    }
}
