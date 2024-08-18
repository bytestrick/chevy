package chevy.control.collectableController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.view.hud.PowerUpText;

public class PowerUpController {
    private final Chamber chamber;
    private final PowerUpText powerUpText;


    public PowerUpController(Chamber chamber, PowerUpText powerUpText) {
        this.chamber = chamber;
        this.powerUpText = powerUpText;
    }


    public void playerInInteraction(Player player, PowerUp powerUp) {
        if (powerUp.changeState(PowerUp.State.COLLECTED)) {
            powerUp.collect();
            chamber.findAndRemoveEntity(powerUp);
            powerUpText.mHide();
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
                    powerUpText.show(powerUp);
            }
            else if (powerUp.checkAndChangeState(PowerUp.State.DESELECTED)) {
                powerUpText.mHide();
            }
            powerUp.checkAndChangeState(PowerUp.State.IDLE);
        }
    }
}
