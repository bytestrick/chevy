package chevy.control.collectableController;

import chevy.control.hudController.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;

public class HealthController {
    private final Chamber chamber;
    private final HUDController hudController;


    public HealthController(HUDController hudController, Chamber chamber) {
        this.chamber = chamber;
        this.hudController = hudController;
    }


    public void playerInInteraction(Player player, Health health) {
        if (health.changeState(Health.State.COLLECTED)) {
            player.increaseCurrentHealth(health.getRecoverHealth());
            health.collect();
            hudController.changeHealth(player.getCurrentHealth());
            chamber.findAndRemoveEntity(health);
        }
    }

    public void update(Health health) {
        if (health.isCollected()) {
            if (health.getState(Health.State.COLLECTED).isFinished()) {
                health.setToDraw(false);
                health.removeToUpdate();
            }
        }
    }
}
