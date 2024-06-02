package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.InteractionType;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
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
            Entity entityNextCell = chamber.getNearEntity(player, direction);

            if (entityNextCell instanceof Enemy enemy) {
                if (player.changeState(PlayerStates.ATTACK)) {
                    enemyController.handleInteraction(InteractionType.PLAYER, player, enemy);
                }
            }
            else if (chamber.canCross(player, direction) && player.changeState(PlayerStates.MOVE)) {
                chamber.moveDynamicEntity(player, direction);
            }
            else
                player.changeState(PlayerStates.IDLE);
        }
        else
            player.changeState(PlayerStates.IDLE);
    }

    public void enemyInteraction(Enemy enemy) {
        switch (enemy.getCurrentEumState()) {
            case BatStates.ATTACK -> {
                if (player.changeState(PlayerStates.HIT))
                    player.changeHealth(-1 * enemy.getDamage());
                if (!player.isAlive() && player.changeState(PlayerStates.DEAD))
                    chamber.removeEntityOnTop(player);
                else
                    player.changeState(PlayerStates.IDLE);
            }
            default -> {}
        }
    }

    public void setEnemyController(EnemyController enemyController) {
        if (this.enemyController == null)
            this.enemyController = enemyController;
    }
}
