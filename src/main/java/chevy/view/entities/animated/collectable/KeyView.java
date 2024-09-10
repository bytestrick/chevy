package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Key;

public final class KeyView extends CollectableView {
    public KeyView(Key key) {
        super(key);

        final String res = "/sprites/collectable/key/";
        final float idleDuration = collectable.getState(Key.State.IDLE).getDuration();
        animate(Key.State.IDLE, null, 12, idleDuration, res + "idle");

        final float collectedDuration = collectable.getState(Key.State.COLLECTED).getDuration();
        animate(Key.State.COLLECTED, null, 7, collectedDuration, res + "collect");
    }
}