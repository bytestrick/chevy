package chevy.control.enemyController;

import chevy.control.PlayerController;
import chevy.model.pathFinding.AStar;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;
import chevy.utilz.Vector2;

import java.util.List;

public class SlimeController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Slime slime) {
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
        // attacca se ai il player di fianco
        DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
        if (direction != null) {
            if (slime.changeState(SlimeStates.ATTACK)) {
                playerController.enemyInteraction(slime);
            }
        }
        // altrimenti muoviti:
        else {
            // muoviti verso il player se si trova dentro il tuo campo visivo
            Player player = chamber.findPlayerInSquareRange(slime, 2);
            System.out.println(player);
            if (player != null) {
                AStar aStar = new AStar(chamber);
                List<Vector2<Integer>> path = aStar.find(slime, player);
                if (path != null) {
                    if (chamber.canCross(path.get(1)) && slime.changeState(SlimeStates.MOVE)) {
                        chamber.moveDynamicEntity(slime, path.get(1));
                    }
                }
            }
            // muoviti in modo casuale
            else {
                direction = chamber.getHitDirectionPlayer(slime);
                if (direction == null) {
                    direction = DirectionsModel.getRandom();
                    if (chamber.canCross(slime, direction) && slime.changeState(SlimeStates.MOVE)) {
                        chamber.moveDynamicEntity(slime, direction);
                    }
                }
            }

            slime.changeState(SlimeStates.IDLE);
        }
    }
}
