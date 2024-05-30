package chevy.control;

import chevy.model.GameModel;
import chevy.model.chamber.Chamber;
import chevy.view.GameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private final GameView gameView;
    private final GameModel gameModel;
    private ChamberController chamberController;

    public KeyboardListener(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;

        // connesione delgli eventi da tastiera al GamePanel
        gameView.getGamePanel().addKeyBoardListener(this);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (chamberController != null)
            chamberController.keyPressed(keyEvent);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    public void setChamber(ChamberController chamberController) {
        this.chamberController = chamberController;
    }
}
