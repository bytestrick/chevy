package chevy.control;

import chevy.utils.Log;
import chevy.view.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private ChamberController chamberController;
    private Window window;

    public KeyboardListener(Window window) {
        this.window = window;
        window.gamePanel.addKeyListener(this);
        window.menu.root.addKeyListener(this);
        window.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (window.getScene()) {
            case Window.Scene.PLAYING -> {
                if (chamberController != null) {
                    chamberController.keyPressed(keyEvent);
                } else {
                    Log.warn("Non posso passare gli eventi della tastiera alla Chamber perché il riferimento è nullo");
                }
            }
            case Window.Scene.MENU -> window.menu.handleKeyPress(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }

    public void setChamber(ChamberController chamberController) { this.chamberController = chamberController; }
}
