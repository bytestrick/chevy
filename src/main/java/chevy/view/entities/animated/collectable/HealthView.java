package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Health;

public final class HealthView extends CollectableView {
    public HealthView(Health health) {
        super(health);

        String res = "/sprites/collectable/health/";
        float idleDuration = collectable.getState(Health.State.IDLE).getDuration();
        animate(Health.State.IDLE, null, 10, idleDuration, res + "idle");

        float collectedDuration = collectable.getState(Health.State.COLLECTED).getDuration();
        animate(Health.State.COLLECTED, null, 7, collectedDuration, res + "collect");
    }
}
