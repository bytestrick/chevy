package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.control.trapsController.TrapsController;
import chevy.control.trapsController.TrapsUpdateController;
import chevy.model.chamber.Chamber;

import java.awt.event.KeyEvent;

public class ChamberController {
    private final PlayerController playerController;


    public ChamberController(Chamber chamber) {
        this.playerController = new PlayerController(chamber, null);
        EnemyController enemyController = new EnemyController(chamber, playerController);
        TrapsController trapsController = new TrapsController(chamber);
        ProjectileController projectileController = new ProjectileController(chamber, playerController, enemyController);

        playerController.setEnemyController(enemyController);
        playerController.setTrapController(trapsController);
        new EnemyUpdateController(enemyController, chamber.getEnemies());
        new TrapsUpdateController(trapsController, chamber.getTraps());
        new ProjectileUpdateController(projectileController, chamber.getProjectiles());
    }

    public void keyPressed(KeyEvent keyEvent) {
        playerController.keyPressed(keyEvent);
    }
}
