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
import chevy.view.GamePanel;

/**
 * Gestisce le interazione tra le diverse entità, nella stanza di gioco
 */
public final class ChamberController {
    private static GamePanel gamePanel;
    private static EnemyUpdateController enemyUpdateController;
    private static TrapsUpdateController trapsUpdateController;
    private static ProjectileUpdateController projectileUpdateController;
    private static CollectableUpdateController collectableUpdateController;
    private static EnvironmentUpdateController environmentUpdateController;

    /**
     * Inizializza (o reimposta) i controller per il giocatore, i nemici, le trappole e i
     * proiettili.
     * Da usare ogni volta che si cambia stanza.
     */
    public static void refresh() {
        final Chamber chamber = ChamberManager.getCurrentChamber();
        final PlayerController playerController = new PlayerController(chamber, gamePanel);
        gamePanel.getWindow().getWindowController().setPlayerController(playerController);

        final EnemyController enemyController = new EnemyController(chamber, playerController);
        playerController.setEnemyController(enemyController);
        if (enemyUpdateController != null) {
            enemyUpdateController.stopUpdate();
        }
        enemyUpdateController = new EnemyUpdateController(enemyController, chamber.getEnemies());

        final TrapsController trapsController = new TrapsController(chamber, playerController,
                enemyController);
        playerController.setTrapController(trapsController);
        if (trapsUpdateController != null) {
            trapsUpdateController.stopUpdate();
        }
        trapsUpdateController = new TrapsUpdateController(trapsController, chamber.getTraps());

        final HUDController hudController = new HUDController(new HUD(), gamePanel.getHudView());
        playerController.setHUDController(hudController);

        CollectableController collectableController = new CollectableController(chamber,
                hudController, enemyUpdateController, projectileUpdateController);
        playerController.setCollectableController(collectableController);
        if (collectableUpdateController != null) {
            collectableUpdateController.stopUpdate();
        }
        collectableUpdateController = new CollectableUpdateController(collectableController,
                chamber.getCollectables());

        final ProjectileController projectileController = new ProjectileController(chamber,
                playerController,
                enemyController);
        playerController.setProjectileController(projectileController);
        if (projectileUpdateController != null) {
            projectileUpdateController.stopUpdate();
        }
        projectileUpdateController = new ProjectileUpdateController(projectileController,
                chamber.getProjectiles());

        final EnvironmentController environmentController =
                new EnvironmentController(chamber, hudController, gamePanel);
        playerController.setEnvironmentController(environmentController);
        if (environmentUpdateController != null) {
            environmentUpdateController.stopUpdate();
        }
        environmentUpdateController = new EnvironmentUpdateController(environmentController,
                chamber.getEnvironments());

        // collega al chamberView la stanza (con le entità ordinate in base al layer) da disegnare
        GamePanel.getChamberView().setDrawOrder(chamber.getDrawOrderChamber());
    }

    public static void setGamePanel(GamePanel gamePanel) {ChamberController.gamePanel = gamePanel;}
}
