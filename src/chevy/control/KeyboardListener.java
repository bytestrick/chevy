package chevy.control;

import chevy.view.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private final Window window;
    private ChamberController chamberController;

    public KeyboardListener(Window window) {
        this.window = window;

        // connesione delgli eventi da tastiera al GamePanel
        window.getGamePanel().addKeyBoardListener(this);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (chamberController != null) {
            chamberController.keyPressed(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }

    public void setChamber(ChamberController chamberController) {
        this.chamberController = chamberController;
    }
}