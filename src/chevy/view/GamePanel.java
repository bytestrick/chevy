package chevy.view;

import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.EnemyTypes;
import chevy.model.entity.dinamicEntity.liveEntity.player.PlayerTypes;
import chevy.model.entity.staticEntity.environment.EnvironmentTypes;
import chevy.model.entity.staticEntity.environment.WallTypes;
import chevy.service.Render;
import chevy.settings.GameSettings;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class GamePanel extends JPanel implements Render {
    private Chamber chamber;

    public GamePanel() {}


    public void setChamber(Chamber chamber) {
        this.chamber = chamber;
    }

    public void addKeyBoardListener(KeyListener keyboardListener) {
        addKeyListener(keyboardListener);
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (chamber == null)
            return;


        for (List<List<Entity>> r : chamber.getChamber()) {
            for (List<Entity> c : r) {
                Entity onTop = chamber.getEntityOnTop(c);
                if (onTop != null) {
                    boolean draw = true;
                    switch (onTop.getSpecificType()) {
                        case PlayerTypes.KNIGHT -> g.setColor(Color.RED);
                        case EnemyTypes.BAT -> g.setColor(Color.magenta);
                        case WallTypes.TOP -> g.setColor(Color.BLACK);
                        case WallTypes.LEFT -> g.setColor(Color.BLACK);
                        case WallTypes.RIGHT -> g.setColor(Color.BLACK);
                        case WallTypes.BOTTOM -> g.setColor(Color.BLACK);
                        default -> draw = false;
                    }

                    if (draw) {
                        g.fillRect(onTop.getCol() * GameSettings.SCALE, onTop.getRow() * GameSettings.SCALE, GameSettings.SCALE, GameSettings.SCALE);
                    }

                }
            }
        }
    }

    @Override
    public void render() {
        repaint();
    }
}