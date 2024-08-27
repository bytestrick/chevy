package chevy.control;

import chevy.control.collectableController.CollectableController;
import chevy.control.collectableController.CollectableUpdateController;
import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.environmentController.EnvironmentController;
import chevy.control.environmentController.EnvironmentUpdateController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.control.trapsController.TrapsController;
import chevy.control.trapsController.TrapsUpdateController;
import chevy.model.HUD;
import chevy.model.chamber.Chamber;
import chevy.model.chamber.ChamberManager;
import chevy.service.Sound;
import chevy.view.Window;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

/**
 * Gestisce le interazione tra le diverse entità, nella stanza di gioco
 */
public class ChamberController {
    private final HUDView hudView;
    private final ChamberView chamberView;
    private final Window window;
    private EnemyUpdateController enemyUpdateController;
    private TrapsUpdateController trapsUpdateController;
    private ProjectileUpdateController projectileUpdateController;
    private CollectableUpdateController collectableUpdateController;
    private EnvironmentUpdateController environmentUpdateController;

    public ChamberController(final Window window) {
        chamberView = window.gamePanel.getChamberView();
        this.window = window;
        hudView = window.gamePanel.getHudView();
        refresh();
    }

    /**
     * Inizializza (o reimposta) i controller per il giocatore, i nemici, le trappole e i proiettili.
     * Da usare ogni volta che si cambia stanza.
     */
    public void refresh() {
        Chamber chamber = ChamberManager.getInstance().getCurrentChamber();
        PlayerController playerController = new PlayerController(chamber, window.gamePanel);

        EnemyController enemyController = new EnemyController(chamber, playerController);
        playerController.setEnemyController(enemyController);
        if (enemyUpdateController != null) {
            enemyUpdateController.updateTerminate();
        }
        enemyUpdateController = new EnemyUpdateController(enemyController, chamber.getEnemies());

        TrapsController trapsController = new TrapsController(chamber, playerController, enemyController);
        playerController.setTrapController(trapsController);
        if (trapsUpdateController != null) {
            trapsUpdateController.updateTerminate();
        }
        trapsUpdateController = new TrapsUpdateController(trapsController, chamber.getTraps());

        HUDController hudController = new HUDController(new HUD(), hudView);
        playerController.setHUDController(hudController);

        CollectableController collectableController = new CollectableController(chamber, playerController,
                hudController);
        playerController.setCollectableController(collectableController);
        if (collectableUpdateController != null) {
            collectableUpdateController.updateTerminate();
        }
        collectableUpdateController = new CollectableUpdateController(collectableController, chamber.getCollectables());

        ProjectileController projectileController = new ProjectileController(chamber, playerController,
                enemyController);
        playerController.setProjectileController(projectileController);
        if (projectileUpdateController != null) {
            projectileUpdateController.updateTerminate();
        }
        projectileUpdateController = new ProjectileUpdateController(projectileController, chamber.getProjectiles());

        EnvironmentController environmentController = new EnvironmentController(chamber, hudController, this);
        playerController.setEnvironmentController(environmentController);
        if (environmentUpdateController != null) {
            environmentUpdateController.updateTerminate();
        }
        environmentUpdateController = new EnvironmentUpdateController(environmentController, chamber.getEnvironment());

        // collega al chamberView la stanza (con le entità ordinate in base al layer) da disegnare
        chamberView.setDrawOrder(chamber.getDrawOrderChamber());
    }
}
