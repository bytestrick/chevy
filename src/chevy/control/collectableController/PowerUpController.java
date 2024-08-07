package chevy.control.collectableController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;

public class PowerUpController {
    private final Chamber chamber;
    private final PowerUpTextVisualizerController powerUpTextVisualizerController;


    public PowerUpController(Chamber chamber, PowerUpTextVisualizerController powerUpTextVisualizerController) {
        this.chamber = chamber;
        this.powerUpTextVisualizerController = powerUpTextVisualizerController;
    }


    public void playerInInteraction(Player player, PowerUp powerUp) {
        if (powerUp.changeState(PowerUp.State.COLLECTED)) {
            powerUp.collect();
            chamber.findAndRemoveEntity(powerUp);
            powerUpTextVisualizerController.hide();
            player.acquirePowerUp((PowerUp.Type) powerUp.getSpecificType(), powerUp);
        }
    }

    public void update(PowerUp powerUp) {
        if (powerUp.isCollected()) {
            if (powerUp.getState(PowerUp.State.COLLECTED).isFinished()) {
                powerUp.setToDraw(false);
                powerUp.removeToUpdate();
            }
        }
        else {
            if (chamber.getHitDirectionPlayer(powerUp) != null) {
                if (powerUp.changeState(PowerUp.State.SELECTED))
                    powerUpTextVisualizerController.show(powerUp);
            }
            else if (powerUp.checkAndChangeState(PowerUp.State.DESELECTED)) {
                    powerUpTextVisualizerController.hide();
            }
            powerUp.checkAndChangeState(PowerUp.State.IDLE);
        }
    }
}
