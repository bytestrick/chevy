package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.trapsController.TrapsController;
import chevy.model.chamber.Chamber;

import java.awt.event.KeyEvent;

public class ChamberController {
    private final PlayerController playerController;
    private final EnemyController enemyController;
    private final TrapsController trapsController;


    public ChamberController(Chamber chamber) {
        this.playerController = new PlayerController(chamber, null);
        this.enemyController = new EnemyController(chamber, playerController);
        this.trapsController = new TrapsController(chamber);

        playerController.setEnemyController(enemyController);
        playerController.setTrapController(trapsController);
        new EnemyUpdateController(enemyController, chamber.getEnemies());
    }


    public void keyPressed(KeyEvent keyEvent) {
        playerController.keyPressed(keyEvent);
    }
}
