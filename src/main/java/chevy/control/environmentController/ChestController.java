package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.Chest;

public final  class ChestController {
    private final Chamber chamber;
    private final HUDController hudController;

    public ChestController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    public void update(Chest chest) {
        if (chamber.getDirectionToHitPlayer(chest) != null) {
            if (chest.checkAndChangeState(Chest.State.OPEN)) {
                if (chest.isFirstOpen()) {
                    chamber.spawnCollectableAroundChest(chest, chest.getSpawnQuantity());
                }
            }
            if (hudController.getKey() > 0 && chest.checkAndChangeState(Chest.State.UNLOCK)) {
                hudController.addKey(-1);
            }
        } else {
            chest.checkAndChangeState(Chest.State.CLOSE);
        }

        chest.checkAndChangeState(Chest.State.IDLE_LOCKED);
        chest.checkAndChangeState(Chest.State.IDLE_UNLOCKED);
    }

    //private void spawnCollectable() { }
}
