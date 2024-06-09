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
    private final ChamberView chamberView;

    public GamePanel() {
        this.chamberView = new ChamberView();

        RenderManager.addToRender(this);
        setBackground(Color.BLACK);
    }


    public void setChamber(Chamber chamber) {
        this.chamber = chamber;
    }

    public ChamberView getChamberView() {
        return chamberView;
    }

    public void addKeyBoardListener(KeyListener keyboardListener) {
        addKeyListener(keyboardListener);
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chamberView.draw(g);

    }

    @Override
    public void render() {
        repaint();
    }
}