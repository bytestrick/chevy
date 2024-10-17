package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.model.chamber.ChamberManager;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.view.GamePanel;
import chevy.view.Menu;

final class StairController {
    static boolean onTrapDoor;
    private static GamePanel gamePanel;
    private static HUDController hudController;

    static void playerInInteraction(Stair stair) {
        if (stair.getState() == Stair.State.IDLE_ENTRY) {
            onTrapDoor = true;
        }
    }

    static void update(Stair stair) {
        if (ChamberManager.getCurrentChamber().getEnemyCounter() == 0) {
            stair.checkAndChangeState(Stair.State.OPEN);
        }

        if (stair.getState() == Stair.State.OPEN && stair.getState(Stair.State.OPEN).isFinished()) {
            stair.changeState(Stair.State.IDLE_ENTRY);
        }

        if (onTrapDoor && ChamberManager.getCurrentChamber().getPlayer().getState(Player.State.MOVE).isFinished()) {
            Data.set("progress.coins", hudController.getCoins());
            Data.set("progress.keys", hudController.getKeys());
            Sound.play(Sound.Effect.WIN);
            Menu.incrementLevel();
            gamePanel.winDialog();
            stair.removeFromUpdate();
        }
    }

    static void setGamePanel(GamePanel gamePanel) {StairController.gamePanel = gamePanel;}

    public static void setHUDController(HUDController hudController) {
        StairController.hudController = hudController;
    }
}