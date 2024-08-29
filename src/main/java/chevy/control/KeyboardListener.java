package chevy.control;

import chevy.view.Window;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private final Window window;
    public static PlayerController playerController;

    public KeyboardListener(Window window) {
        this.window = window;
        window.gamePanel.addKeyListener(this);
        window.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (window.getScene()) {
            case Window.Scene.PLAYING -> {
                assert playerController != null;
                playerController.keyPressed(keyEvent);
            }
            case Window.Scene.MENU -> window.menu.handleKeyPress(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) { }
}
