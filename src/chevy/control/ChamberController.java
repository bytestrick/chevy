package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.control.trapsController.TrapsController;
import chevy.control.trapsController.TrapsUpdateController;
import chevy.model.chamber.Chamber;

import java.awt.event.KeyEvent;

/**
 * La classe ChamberController gestisce l'interazione tra il giocatore e la stanza di gioco.
 * Questa classe si occupa di inizializzare i controller per il giocatore, i nemici, le trappole e i proiettili.
 */
public class ChamberController {
    /**
     * Riferimento al controller del giocatore.
     */
    private final PlayerController playerController;

    /**
     * Inizializza il controller della stanza con i riferimenti alla stanza di gioco.
     * Crea i controller per il giocatore, i nemici, le trappole e i proiettili.
     * @param chamber riferimento alla stanza di gioco
     */
    public ChamberController(Chamber chamber) {
        this.playerController = new PlayerController(chamber);
        EnemyController enemyController = new EnemyController(chamber, playerController);
        TrapsController trapsController = new TrapsController(chamber, playerController);
        ProjectileController projectileController = new ProjectileController(chamber, playerController, enemyController);

        playerController.setEnemyController(enemyController);
        playerController.setTrapController(trapsController);
        playerController.setProjectileController(projectileController);
        new EnemyUpdateController(enemyController, chamber.getEnemies());
        new TrapsUpdateController(trapsController, chamber.getTraps());
        new ProjectileUpdateController(projectileController, chamber.getProjectiles());
    }

    /**
     * Gestisce gli eventi di pressione dei tasti delegandoli al controller del giocatore.
     * @param keyEvent l'evento di pressione del tasto
     */
    public void keyPressed(KeyEvent keyEvent) {
        playerController.keyPressed(keyEvent);
    }
}
