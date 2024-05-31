package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;

import java.awt.event.KeyEvent;

public class ChamberController {
    private Chamber chamber;

    private PlayerController playerController;
    private EnemyController enemyController;


    public ChamberController(Chamber chamber) {
        this.chamber = chamber;
        this.enemyController = new EnemyController(chamber);
        this.playerController = new PlayerController(chamber, enemyController);
    }


    public void keyPressed(KeyEvent keyEvent) {
        playerController.keyPressed(keyEvent);
        chamber.show();
    }
}
