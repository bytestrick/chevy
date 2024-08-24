package chevy.control;

import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.view.Window;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowController implements WindowListener {
    private final Window window;

    public WindowController(final Window window) { this.window = window; }

    @Override
    public void windowOpened(WindowEvent windowEvent) { }

    @Override
    public void windowClosing(WindowEvent windowEvent) { window.quitAction(); }

    @Override
    public void windowClosed(WindowEvent windowEvent) { }

    @Override
    public void windowIconified(WindowEvent windowEvent) {
        if (window.getScene() == Window.Scene.PLAYING) {
            GameLoop.getInstance().pause();
            Sound.getInstance().pauseMusic();
        }
    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {
        if (window.getScene() == Window.Scene.PLAYING) {
            window.gamePanel.pauseDialog();
        }
    }

    @Override
    public void windowActivated(WindowEvent windowEvent) { }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) { }
}