package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.model.chamber.Chamber;

import java.awt.event.KeyEvent;

public class ChamberController {
    private final Chamber chamber;
    private final PlayerController playerController;
    private final EnemyController enemyController;


    public ChamberController(Chamber chamber) {
        this.chamber = chamber;
        this.playerController = new PlayerController(chamber, null);
        this.enemyController = new EnemyController(chamber, playerController);
        playerController.setEnemyController(enemyController);
        new EnemyUpdateController(enemyController, chamber.getEnemies());
    }


    public void keyPressed(KeyEvent keyEvent) {
        playerController.keyPressed(keyEvent);
    }
}
