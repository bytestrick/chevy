package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.model.chamber.ChamberManager;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.view.GamePanel;
import chevy.view.Menu;

public final class StairController {
    private static GamePanel gamePanel;
    private static HUDController hudController;

    public static void playerInInteraction(Stair stair) {
        if (stair.getCurrentState() == Stair.State.IDLE_ENTRY) {
            Sound.play(Sound.Effect.WIN);
            Menu.incrementLevel();
            gamePanel.winDialog();
        }
    }

    public static void update(Stair stair) {
        if (ChamberManager.getCurrentChamber().getEnemyCounter() == 0) {
            stair.checkAndChangeState(Stair.State.OPEN);
        }

        if (stair.getCurrentState() == Stair.State.OPEN && stair.getState(Stair.State.OPEN).isFinished()) {
            stair.changeState(Stair.State.IDLE_ENTRY);
            hudController.addKey(0);
            hudController.addCoin(0);
            stair.removeFromUpdate();
        }
    }

    public static void setGamePanel(GamePanel gp) {
        gamePanel = gp;
    }

    public static void setHUDController(HUDController hudc) {
        hudController = hudc;
    }
}
