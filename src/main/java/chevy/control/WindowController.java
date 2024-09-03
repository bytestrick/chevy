package chevy.control;

import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.view.Window;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Cattura gli eventi di Window
 */
public class WindowController implements WindowListener, KeyListener, ComponentListener {
    public static PlayerController playerController;
    private final Window window;

    public WindowController(final Window window) {
        this.window = window;
        this.window.addKeyListener(this);
        this.window.addWindowListener(this);
        this.window.addComponentListener(this);
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {}

    @Override
    public void windowClosing(WindowEvent windowEvent) {window.quitAction();}

    @Override
    public void windowClosed(WindowEvent windowEvent) {}

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        switch (window.getScene()) {
            case PLAYING -> {
                GameLoop.stop();
                Sound.pauseMusic();
            }
            case MENU, OPTIONS -> Sound.stopMenuMusic();
        }
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
        switch (window.getScene()) {
            case PLAYING -> window.getGamePanel().pauseDialog();
            case MENU, OPTIONS -> Sound.startMenuMusic();
        }
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {}

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {}

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (window.getScene()) {
            case Window.Scene.PLAYING -> {
                assert playerController != null;
                playerController.keyPressed(keyEvent);
            }
            case Window.Scene.MENU -> window.getMenu().handleKeyPress(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {}

    @Override
    public void componentResized(ComponentEvent e) {
        Window.updateSize(window.getSize());
        window.getGamePanel().windowResized(Window.scale);
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}