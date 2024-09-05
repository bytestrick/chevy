package chevy.control.environmentController;

import chevy.model.chamber.ChamberManager;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.service.Sound;
import chevy.view.GamePanel;
import chevy.view.Menu;

public final class StairController {
    private static GamePanel gamePanel;
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
            stair.removeFromUpdate();
        }
    }

    public static void setGamePanel(GamePanel gp) {
        gamePanel = gp;
    }
}
