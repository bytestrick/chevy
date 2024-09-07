package chevy.control;

import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.view.Window;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Cattura gli eventi di Window
 */
public final class WindowController extends KeyAdapter implements WindowListener, ComponentListener,
                                                                  MouseListener {
    private final Window window;
    private PlayerController playerController;

    public WindowController(final Window window) {
        this.window = window;
        this.window.addWindowListener(this);
        this.window.addComponentListener(this);
        this.window.addKeyListener(this);
        this.window.addMouseListener(this);
    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        switch (window.getScene()) {
            case PLAYING -> {
                GameLoop.stop();
                Sound.stopMusic();
            }
            case MENU, OPTIONS -> Sound.stopLoop();
        }
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
        switch (window.getScene()) {
            case PLAYING -> window.getGamePanel().pauseDialog();
            case MENU, OPTIONS -> Sound.startLoop(Sound.Music.SAME_SONG);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Window.updateSize(window.getSize());
        window.getGamePanel().windowResized(Window.scale);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (playerController != null) {
            playerController.mousePressed(mouseEvent.getPoint());
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (window.getScene()) {
            case PLAYING -> playerController.keyPressed(keyEvent);
            case TUTORIAL -> window.getGamePanel().getTutorial().keyPressed(keyEvent);
        }
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {}

    @Override
    public void windowClosing(WindowEvent windowEvent) {window.quitAction();}

    @Override
    public void windowClosed(WindowEvent windowEvent) {}

    @Override
    public void windowActivated(WindowEvent windowEvent) {}

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {}

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}