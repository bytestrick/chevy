package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Key;
import chevy.service.Data;
import chevy.service.Sound;

public final class KeyController {
    private final Chamber chamber;
    private final HUDController hudController;

    public KeyController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    public void playerInInteraction(Key key) {
        if (key.changeState(Key.State.COLLECTED)) {
            Sound.play(Sound.Effect.KEY_EQUIPPED);
            key.collect();
            Data.increment("progress.keys");
            Data.increment("stats.collectable.total.count");
            Data.increment("stats.collectable.commons.keys.count");
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
