package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.service.Sound;

final class ChestController {
    private final Chamber chamber;
    private final HUDController hudController;

    ChestController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    void update(Chest chest) {
        if (chamber.getDirectionToHitPlayer(chest) != null) {
            if (chest.checkAndChangeState(Chest.State.OPEN)) {
                Sound.play(Sound.Effect.CHEST_OPEN);
                if (chest.isFirstOpen()) {
                    Sound.play(Sound.Effect.CHEST_GLARE);
                    chamber.spawnCollectableAroundChest(chest, Chest.getSpawnQuantity());
                }
            }
            if (hudController.getKeys() > 0 && chest.checkAndChangeState(Chest.State.UNLOCK)) {
                hudController.addKeys(-1);
            }
        } else {
            if (chest.checkAndChangeState(Chest.State.CLOSE)) {
                Sound.play(Sound.Effect.CHEST_CLOSE);
            }
        }

        chest.checkAndChangeState(Chest.State.IDLE_LOCKED);
        chest.checkAndChangeState(Chest.State.IDLE_UNLOCKED);
    }
}