package chevy.control.collectableController;

import chevy.Sound;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Health;

public class HealthController {
    private final Chamber chamber;


    public HealthController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Health health) {
        if (health.changeState(Health.State.COLLECTED)) {
            Sound.getInstance().play(Sound.Effect.HEALTH_POTION);
            health.collect();
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
