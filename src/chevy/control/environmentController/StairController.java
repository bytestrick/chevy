package chevy.control.environmentController;

import chevy.model.chamber.ChamberManager;
import chevy.model.entity.staticEntity.environment.Stair;

public class StairController {
    public static void playerInInteraction(Stair stair) {
        if (stair.getCurrentState() == Stair.State.IDLE_ENTRY) {
            ChamberManager.nextChamber();
        }
    }

    public static void update(Stair stair) {
        if (ChamberManager.getCurrentChamber().getEnemyCounter() == 0) {
            stair.checkAndChangeState(Stair.State.OPEN);
        }

        if (stair.getCurrentState() == Stair.State.OPEN && stair.getState(Stair.State.OPEN).isFinished()) {
            stair.changeState(Stair.State.IDLE_ENTRY);
            stair.removeFromUpdate();
        }
    }
}
