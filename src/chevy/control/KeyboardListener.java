package chevy.control;

import chevy.view.GameView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private final GameView gameView;
    private ChamberController chamberController;


    public KeyboardListener(GameView gameView) {
        this.gameView = gameView;

        // connesione delgli eventi da tastiera al GamePanel
        gameView.getWindow().getGamePanel().addKeyBoardListener(this);
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
