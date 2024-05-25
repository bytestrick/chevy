package chevy.control;

import chevy.Game;
import chevy.model.dinamicEntity.Directions;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private final Game game;

    public KeyboardListener(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()) {
            case KeyEvent.VK_W -> game.player.move(Directions.UP);
            case KeyEvent.VK_A -> game.player.move(Directions.LEFT);
            case KeyEvent.VK_S -> game.player.move(Directions.DOWN);
            case KeyEvent.VK_D -> game.player.move(Directions.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
