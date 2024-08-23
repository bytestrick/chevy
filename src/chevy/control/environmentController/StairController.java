package chevy.control.environmentController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.Stair;

public class StairController {
    private final Chamber chamber;

    public StairController(Chamber chamber) {
        this.chamber = chamber;
    }

    public void update(Stair stair) {
        if (chamber.getEnemyCounter() == 0)
            stair.checkAndChangeState(Stair.State.OPEN);

        if (stair.getCurrentState() == Stair.State.OPEN && stair.getState(Stair.State.OPEN).isFinished()) {
            stair.changeState(Stair.State.IDLE_ENTRY);
            stair.removeToUpdate();
        }
    }
}
