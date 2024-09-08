package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Key;

public final class KeyView extends CollectableView {
    private static final String KEY_RES = "/sprites/collectable/key/";

    public KeyView(Key key) {super(key);}

    @Override
    protected void initializeAnimation() {
        final float idleDuration = collectable.getState(Key.State.IDLE).getDuration();
        animate(Key.State.IDLE, null, 12, idleDuration, KEY_RES + "idle");

        final float collectedDuration = collectable.getState(Key.State.COLLECTED).getDuration();
        animate(Key.State.COLLECTED, null, 7, collectedDuration, KEY_RES + "collect");
    }
}