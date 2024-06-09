package chevy.view;

import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.EnemyTypes;
import chevy.model.entity.dinamicEntity.liveEntity.player.PlayerTypes;
import chevy.model.entity.dinamicEntity.projectile.ProjectileTypes;
import chevy.model.entity.staticEntity.environment.EnvironmentTypes;
import chevy.model.entity.staticEntity.environment.WallTypes;
import chevy.model.entity.staticEntity.environment.traps.TrapsTypes;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.settings.GameSettings;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class GamePanel extends JPanel implements Render {
    private Chamber chamber;

    public GamePanel() {
        RenderManager.addToRender(this);
        setBackground(Color.BLACK);
    }


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
                for (Entity onTop : c) {
                    if (onTop != null) {
                        boolean draw = true;
                        switch (onTop.getSpecificType()) {
                            case PlayerTypes.KNIGHT -> g.setColor(Color.RED);
                            case EnemyTypes.BAT -> g.setColor(Color.MAGENTA);
                            case EnemyTypes.SLIME -> g.setColor(Color.GREEN);
                            case EnemyTypes.ZOMBIE -> g.setColor(Color.BLUE);
                            case EnemyTypes.BIG_SLIME -> g.setColor(Color.PINK);
                            case TrapsTypes.VOID -> g.setColor(Color.BLACK);
                            case TrapsTypes.ICY_FLOOR -> g.setColor(Color.CYAN);
                            case TrapsTypes.SLUDGE -> g.setColor(Color.BLUE);
                            case TrapsTypes.TRAPDOOR -> g.setColor(Color.LIGHT_GRAY);
                            case TrapsTypes.SPIKED_FLOOR -> g.setColor(Color.GRAY);
                            case TrapsTypes.TOTEM -> g.setColor(Color.ORANGE);
                            case ProjectileTypes.ARROW -> g.setColor(Color.WHITE);
                            case EnemyTypes.SKELETON -> g.setColor(Color.YELLOW);
                            case EnemyTypes.FROG -> g.setColor(Color.GREEN);
                            default -> draw = false;
                        }

                        if (draw) {
                            g.fillRect(onTop.getCol() * GameSettings.SCALE, onTop.getRow() * GameSettings.SCALE, GameSettings.SCALE, GameSettings.SCALE);
                        }
                        draw = true;

                        switch (onTop.getGenericType()) {
                            case EnvironmentTypes.WALL -> g.setColor(Color.YELLOW);
                            case EnvironmentTypes.GROUND -> g.setColor(Color.DARK_GRAY);
                            default -> draw = false;
                        }

                        if (draw) {
                            g.fillRect(onTop.getCol() * GameSettings.SCALE, onTop.getRow() * GameSettings.SCALE, GameSettings.SCALE, GameSettings.SCALE);
                        }
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