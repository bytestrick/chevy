package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.utilz.Vector2;

import java.awt.event.KeyEvent;

public class PlayerController {
    private Chamber chamber;
    private Player player;
    private EnemyController enemyController;

    public PlayerController(Chamber chamber, EnemyController enemyController) {
        this.chamber = chamber;
        this.player = chamber.getPlayer();
        this.enemyController = enemyController;
    }


    public void keyPressed(KeyEvent keyEvent) {
        DirectionsModel direction = switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W -> DirectionsModel.UP;
            case KeyEvent.VK_A -> DirectionsModel.LEFT;
            case KeyEvent.VK_S -> DirectionsModel.DOWN;
            case KeyEvent.VK_D -> DirectionsModel.RIGHT;
            default -> null;
        };

        if (direction != null) {
            Vector2<Integer> currentPosition = new Vector2<>(
                    player.getRow(),
                    player.getCol()
            );
            Vector2<Integer> nextPosition = new Vector2<>(
                    currentPosition.first() + direction.col(),
                    currentPosition.second() + direction.row()
            );
            Entity entityNextCell = chamber.getEntityOnTop(nextPosition);

            if (chamber.canCross(nextPosition) && player.changeState(PlayerStates.MOVE)) {
                chamber.movePlayer(nextPosition);
                System.out.println("Il " + player.toString() + " si è mosso");
            }
            else if (entityNextCell instanceof Enemy enemy)
                if (player.changeState(PlayerStates.ATTACK)) {
                    enemyController.playerInteraction(enemy, (PlayerStates) player.getCurrentEumState(), player.getDamage());
                    System.out.println("Il player ha attaccato");
                }
        }

        player.changeState(PlayerStates.IDLE);
    }
}
