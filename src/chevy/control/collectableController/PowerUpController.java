package chevy.control.collectableController;

import chevy.control.hudController.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;

public class PowerUpController {
    private final Chamber chamber;
    private final HUDController hudController;


    public PowerUpController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }


    public void playerInInteraction(Player player, PowerUp powerUp) {
        if (powerUp.changeState(PowerUp.State.COLLECTED)) {
            powerUp.collect();
            chamber.findAndRemoveEntity(powerUp);
            hudController.hidePowerUpText();
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
                    hudController.PowerUpText(powerUp);
            }
            else if (powerUp.checkAndChangeState(PowerUp.State.DESELECTED)) {
                hudController.hidePowerUpText();
            }
            powerUp.checkAndChangeState(PowerUp.State.IDLE);
        }
    }
}
