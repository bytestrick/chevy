package chevy.control.collectableController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Key;

public class KeyController {
    private final Chamber chamber;


    public KeyController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Key key) {
        if (key.changeState(Key.EnumState.COLLECT)) {
            key.collect();
            chamber.findAndRemoveEntity(key);
        }
    }

    public void update(Key key) {
        if (key.isCollected()) {
            if (key.getState(Key.EnumState.COLLECT).isFinished()) {
                key.setToDraw(false);
                key.removeToUpdate();
            }
        }
    }
}
