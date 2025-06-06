package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Key;
import chevy.service.Data;
import chevy.service.Sound;

final class KeyController {
    private final Chamber chamber;
    private final HUDController hudController;

    KeyController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    static void update(Key key) {
        if (key.isCollected()) {
            if (key.getState(Key.State.COLLECTED).isFinished()) {
                key.setShouldDraw(false);
                key.removeFromUpdate();
            }
        }
    }

    void playerInInteraction(Key key) {
        if (key.changeState(Key.State.COLLECTED)) {
            Sound.play(Sound.Effect.KEY_EQUIPPED);
            key.collect();
            Data.increment("stats.collectable.totalCollected.count");
            Data.increment("stats.collectable.commons.keys.count");
            hudController.addKeys(1);
            chamber.findAndRemoveEntity(key);
        }
    }
}
