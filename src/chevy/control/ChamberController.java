package chevy.control;

import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.KnightStates;
import chevy.utilz.Vector2;

import java.awt.event.KeyEvent;

public class ChamberController {
    private Chamber chamber;
    private Player player;

    public ChamberController(Chamber chamber) {
        this.chamber = chamber;
        this.player = chamber.getPlayer();
    }


    public void keyPressed(KeyEvent keyEvent) {
        playerKeyPressed(keyEvent);
    }

    private void playerKeyPressed(KeyEvent keyEvent) {
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

            if (chamber.canCross(nextPosition) && player.changeState(KnightStates.MOVE)) {
                chamber.movePlayer(nextPosition);
                System.out.println("Il player si è mosso");
            }
            else if (entityNextCell instanceof Enemy enemy && player.changeState(KnightStates.ATTACK)) {
                System.out.println("Il player ha attaccato");
                enemy.changeHealth(-1 * player.getDamage());
                if (!enemy.isAlive())
                    chamber.removeEntityOnTop(enemy);
            }
        }

        player.changeState(KnightStates.IDLE);
        chamber.show();
    }

}
