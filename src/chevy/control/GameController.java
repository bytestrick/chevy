package chevy.control;

import chevy.Game;
import chevy.model.dinamicEntity.Directions;
import chevy.view.Window;

import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController implements KeyListener {
    private final Game game;
    private final Window window;

    public GameController(Game game, Window window) {
        this.game = game;
        this.window = window;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) { }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (window.scene == Window.Scene.PLAYING) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_W -> game.player.move(Directions.UP);
                case KeyEvent.VK_A -> game.player.move(Directions.LEFT);
                case KeyEvent.VK_S -> game.player.move(Directions.DOWN);
                case KeyEvent.VK_D -> game.player.move(Directions.RIGHT);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            // Qui costruiamo un menu in gioco da cui si può
            // - tornare al menù principale
            // - chiudere il gioco
            // - aprire le impostazioni
            if (JOptionPane.showConfirmDialog(window, "Vuoi tornare al menu principale?") == JOptionPane.YES_OPTION) {
                window.setScene(Window.Scene.MENU);
            }
        }
    }
}