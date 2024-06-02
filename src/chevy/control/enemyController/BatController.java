package chevy.control.enemyController;

import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Bat;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.service.Update;

public class BatController  {
    private final Chamber chamber;
    private final PlayerController playerController;


    public BatController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInteraction(Player player, Bat bat) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK -> {
                if (bat.changeState(BatStates.HIT))
                    bat.changeHealth(-1 * player.getDamage());
                if (!bat.isAlive() && bat.changeState(BatStates.DEAD)) {
                    chamber.removeEnemyFormEnemies(bat);
                    chamber.removeEntityOnTop(bat);
                }
                else
                    bat.changeState(BatStates.IDLE);
            }
            default -> System.out.println("Il BatController non gestisce questa azione");
        }
    }

    public void update(Bat bat) {
        DirectionsModel direction = chamber.getHitDirectionPlayer(bat);
        if (direction == null) {
            direction = DirectionsModel.getRandom();
            if (chamber.canCross(bat, direction) && bat.changeState(BatStates.MOVE)) {
                chamber.moveDynamicEntity(bat, direction);
            }
        }
        else {
            Entity entity = chamber.getNearEntity(bat, chamber.getHitDirectionPlayer(bat));
            if (entity instanceof Player && bat.changeState(BatStates.ATTACK)) {
                playerController.enemyInteraction(bat);
            }
        }

        bat.changeState(BatStates.IDLE);
    }
}
