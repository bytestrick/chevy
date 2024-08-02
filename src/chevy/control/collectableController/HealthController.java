package chevy.control.collectableController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Health;

public class HealthController {
    private final Chamber chamber;


    public HealthController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Health health) {
        if (health.changeState(Health.EnumState.COLLECTED)) {
            health.collect();
            chamber.findAndRemoveEntity(health);
        }
    }

    public void update(Health health) {
        if (health.isCollected()) {
            if (health.getState(Health.EnumState.COLLECTED).isFinished()) {
                health.setToDraw(false);
                health.removeToUpdate();
            }
        }
    }
}
