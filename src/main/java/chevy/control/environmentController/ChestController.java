package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.Chest;

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
                if (chest.isFirstOpen()) {
                    chamber.spawnCollectableAroundChest(chest, Chest.getSpawnQuantity());
                }
            }
            if (hudController.getKeys() > 0 && chest.checkAndChangeState(Chest.State.UNLOCK)) {
                hudController.addKeys(-1);
            }
        } else {
            chest.checkAndChangeState(Chest.State.CLOSE);
        }

        chest.checkAndChangeState(Chest.State.IDLE_LOCKED);
        chest.checkAndChangeState(Chest.State.IDLE_UNLOCKED);
    }
}