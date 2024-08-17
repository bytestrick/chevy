package chevy.control.collectableController;

import chevy.Sound;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Key;

public class KeyController {
    private final Chamber chamber;


    public KeyController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Key key) {
        if (key.changeState(Key.State.COLLECTED)) {
            Sound.getInstance().play(Sound.Effect.KEY_EQUIPPED);
            key.collect();
            chamber.findAndRemoveEntity(key);
        }
    }

    public void update(Key key) {
        if (key.isCollected()) {
            if (key.getState(Key.State.COLLECTED).isFinished()) {
                key.setToDraw(false);
                key.removeToUpdate();
            }
        }
    }
}
