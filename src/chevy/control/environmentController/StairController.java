package chevy.control.environmentController;

import chevy.control.ChamberController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.Stair;

public class StairController {
    private final Chamber chamber;
    private final ChamberController chamberController;

    public StairController(Chamber chamber, ChamberController chamberController) {
        this.chamber = chamber;
        this.chamberController = chamberController;
    }


    public void playerInInteraction(Stair stair) {
        if (stair.getCurrentState() == Stair.State.IDLE_ENTRY)
            chamberController.changeNextChamber();
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
