package chevy.control.enemyController;

import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Bat;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;

public class SlimeController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInteraction(Player player, Slime slime) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK -> {
                if (slime.changeState(SlimeStates.HIT))
                    slime.changeHealth(-1 * player.getDamage());
                if (!slime.isAlive() && slime.changeState(SlimeStates.DEAD)) {
                    chamber.removeEnemyFormEnemies(slime);
                    chamber.removeEntityOnTop(slime);
                }
                else
                    slime.changeState(SlimeStates.IDLE);
            }
            default -> System.out.println("Lo slimeController non gestisce questa azione");
        }
    }

    public void update(Slime slime) {
        DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
        if (direction == null) {
            direction = DirectionsModel.getRandom();
            if (chamber.canCross(slime, direction) && slime.changeState(SlimeStates.MOVE)) {
                chamber.moveDynamicEntity(slime, direction);
            }
        }
        else {
            Entity entity = chamber.getNearEntity(slime, chamber.getHitDirectionPlayer(slime));
            if (entity instanceof Player && slime.changeState(BatStates.ATTACK)) {
                playerController.enemyInteraction(slime);
            }
        }

        slime.changeState(SlimeStates.IDLE);
    }
}
