package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Key;

public class KeyController {
    private final Chamber chamber;
    private final HUDController hudController;


    public KeyController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }


    public void playerInInteraction(Key key) {
        if (key.changeState(Key.State.COLLECTED)) {
            key.collect();
            hudController.addKey(1);
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
