package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.model.Statistics;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Key;
import chevy.service.Sound;

public class KeyController {
    private final Chamber chamber;
    private final HUDController hudController;

    public KeyController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    public void playerInInteraction(Key key) {
        if (key.changeState(Key.State.COLLECTED)) {
            Sound.getInstance().play(Sound.Effect.KEY_EQUIPPED);
            key.collect();
            Statistics.increase(Statistics.COLLECTED_COLLECTABLE, 1);
            Statistics.increase(Statistics.COLLECTED_KEY, 1);
            hudController.addKey(1);
            chamber.findAndRemoveEntity(key);
        }
    }

    public void update(Key key) {
        if (key.isCollected()) {
            if (key.getState(Key.State.COLLECTED).isFinished()) {
                key.setToDraw(false);
                key.removeFromUpdate();
            }
        }
    }
}
