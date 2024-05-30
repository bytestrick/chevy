package chevy.control;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.KnightStates;

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
        DirectionsModel direction = null;

        direction = switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W -> DirectionsModel.UP;
            case KeyEvent.VK_A -> DirectionsModel.LEFT;
            case KeyEvent.VK_S -> DirectionsModel.DOWN;
            case KeyEvent.VK_D -> DirectionsModel.RIGHT;
            default -> null;
        };

        if (direction != null && player.changeState(KnightStates.MOVE)) {
            chamber.movePlayer(direction);
            player.changeState(KnightStates.IDLE);
        }
    }

}
