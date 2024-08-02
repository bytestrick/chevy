package chevy.control.collectableController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.PowerUp;

public class PowerUpController {
    private final Chamber chamber;
    private final PowerUpTextVisualizerController powerUpTextVisualizerController;


    public PowerUpController(Chamber chamber, PowerUpTextVisualizerController powerUpTextVisualizerController) {
        this.chamber = chamber;
        this.powerUpTextVisualizerController = powerUpTextVisualizerController;
    }


    public void playerInInteraction(PowerUp powerUp) {
        if (powerUp.changeState(PowerUp.EnumState.COLLECTED)) {
            powerUp.collect();
            chamber.findAndRemoveEntity(powerUp);
        }
    }

    public void update(PowerUp powerUp) {
        if (powerUp.isCollected()) {
            powerUpTextVisualizerController.hide();
            if (powerUp.getState(PowerUp.EnumState.COLLECTED).isFinished()) {
                powerUp.setToDraw(false);
                powerUp.removeToUpdate();
            }
        }
        else {
            if (chamber.getHitDirectionPlayer(powerUp) != null) {
                if (powerUp.changeState(PowerUp.EnumState.SELECTED))
                    powerUpTextVisualizerController.show(powerUp);
            }
            else if (powerUp.checkAndChangeState(PowerUp.EnumState.DESELECTED)) {
                    powerUpTextVisualizerController.hide();
            }
            powerUp.checkAndChangeState(PowerUp.EnumState.IDLE);
        }
    }
}
