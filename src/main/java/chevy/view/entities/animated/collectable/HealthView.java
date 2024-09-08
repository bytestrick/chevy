package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Health;

public final class HealthView extends CollectableView {
    private static final String HEALTH_RES = "/sprites/collectable/health/";

    public HealthView(Health health) {super(health);}

    @Override
    protected void initializeAnimation() {
        final float idleDuration = collectable.getState(Health.State.IDLE).getDuration();
        animate(Health.State.IDLE, null, 10, idleDuration, HEALTH_RES + "idle");

        final float collectedDuration = collectable.getState(Health.State.COLLECTED).getDuration();
        animate(Health.State.COLLECTED, null, 7, collectedDuration, HEALTH_RES + "collect");
    }
}