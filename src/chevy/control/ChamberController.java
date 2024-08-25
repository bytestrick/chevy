package chevy.control;

import chevy.control.environmentController.EnvironmentController;
import chevy.control.collectableController.CollectableController;
import chevy.control.collectableController.CollectableUpdateController;
import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.environmentController.EnvironmentUpdateController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.control.trapsController.TrapsController;
import chevy.control.trapsController.TrapsUpdateController;
import chevy.model.HUD;
import chevy.model.chamber.Chamber;
import chevy.model.chamber.ChamberManager;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

import java.awt.event.KeyEvent;

/**
 * Gestisce l'interazione tra il giocatore e la stanza di gioco.
 * Inizializza i controller per il giocatore, i nemici, le trappole e i proiettili.
 */
public class ChamberController {
    /**
     * Riferimento al controller del giocatore.
     */
    private PlayerController playerController;
    private Chamber chamber;
    private HUDView hudView;
    private ChamberView chamberView;
    private EnemyUpdateController enemyUpdateController;
    private TrapsUpdateController trapsUpdateController;
    private ProjectileUpdateController projectileUpdateController;
    private CollectableUpdateController collectableUpdateController;
    private EnvironmentUpdateController environmentUpdateController;

    /**
     * Inizializza il controller della stanza con i riferimenti alla stanza di gioco.
     * Crea i controller per il giocatore, i nemici, le trappole e i proiettili.
     *
     * @param chamber riferimento alla stanza di gioco
     */
    public ChamberController(ChamberView chamberView, Chamber chamber, HUDView hudView) {
        this.chamber = chamber;
        this.hudView = hudView;
        this.chamberView = chamberView;
        this.playerController = new PlayerController(chamber);
        EnemyController enemyController = new EnemyController(chamber, playerController);
        TrapsController trapsController = new TrapsController(chamber, playerController, enemyController);
        HUDController hudController = new HUDController(new HUD(), hudView);
        CollectableController collectableController = new CollectableController(chamber, playerController, hudController);
        ProjectileController projectileController = new ProjectileController(chamber, playerController, enemyController);
        EnvironmentController environmentController = new EnvironmentController(chamber, hudController, this);

        playerController.setEnemyController(enemyController);
        playerController.setTrapController(trapsController);
        playerController.setProjectileController(projectileController);
        playerController.setCollectableController(collectableController);
        playerController.setEnvironmentController(environmentController);
        playerController.setHUDController(hudController);

        enemyUpdateController = new EnemyUpdateController(enemyController, chamber.getEnemies());
        trapsUpdateController = new TrapsUpdateController(trapsController, chamber.getTraps());
        projectileUpdateController = new ProjectileUpdateController(projectileController, chamber.getProjectiles());
        collectableUpdateController = new CollectableUpdateController(collectableController, chamber.getCollectables());
        environmentUpdateController =  new EnvironmentUpdateController(environmentController, chamber.getEnvironment());

        // collega al chamberView la stanza (con le entità ordinate in base al layer) da disegnare
        chamberView.setDrawOrder(ChamberManager.getInstance().getCurrentChamber().getDrawOrderChamber());
    }

    /**
     * Gestisce gli eventi di pressione dei tasti delegandoli al controller del giocatore.
     *
     * @param keyEvent l'evento di pressione del tasto
     */
    public void keyPressed(KeyEvent keyEvent) {
        playerController.keyPressed(keyEvent);
    }

    public Chamber getChamber() {
        return chamber;
    }

    public void changeNextChamber() {
        this.chamber = ChamberManager.getInstance().getNextChamber();

        this.playerController = new PlayerController(chamber);
        EnemyController enemyController = new EnemyController(chamber, playerController);
        TrapsController trapsController = new TrapsController(chamber, playerController, enemyController);
        HUDController hudController = new HUDController(new HUD(), hudView);
        CollectableController collectableController = new CollectableController(chamber, playerController, hudController);
        ProjectileController projectileController = new ProjectileController(chamber, playerController, enemyController);
        EnvironmentController environmentController = new EnvironmentController(chamber, hudController, this);

        playerController.setEnemyController(enemyController);
        playerController.setTrapController(trapsController);
        playerController.setProjectileController(projectileController);
        playerController.setCollectableController(collectableController);
        playerController.setEnvironmentController(environmentController);
        playerController.setHUDController(hudController);

        enemyUpdateController.updateTerminate();
        enemyUpdateController = new EnemyUpdateController(enemyController, chamber.getEnemies());
        trapsUpdateController.updateTerminate();
        trapsUpdateController = new TrapsUpdateController(trapsController, chamber.getTraps());
        projectileUpdateController.updateTerminate();
        projectileUpdateController = new ProjectileUpdateController(projectileController, chamber.getProjectiles());
        collectableUpdateController.updateTerminate();
        collectableUpdateController = new CollectableUpdateController(collectableController, chamber.getCollectables());
        environmentUpdateController.updateTerminate();
        environmentUpdateController =  new EnvironmentUpdateController(environmentController, chamber.getEnvironment());

        // collega al chamberView la stanza (con le entità ordinate in base al layer) da disegnare
        chamberView.setDrawOrder(ChamberManager.getInstance().getCurrentChamber().getDrawOrderChamber());
    }
}
