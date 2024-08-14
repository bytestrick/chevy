package chevy.view;

import chevy.model.entity.collectable.Health;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.settings.WindowSettings;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUD;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel {
    private final ChamberView chamberView;
    private final HUD hud;

    public GamePanel() {
        this.chamberView = new ChamberView();
        this.hud = new HUD();

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        springLayout.putConstraint(SpringLayout.NORTH, chamberView, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, chamberView, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, chamberView, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, chamberView, 0, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.NORTH, hud, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, hud, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, hud, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, hud, 0, SpringLayout.SOUTH, this);

        add(hud);
        add(chamberView);
        setBackground(Color.BLACK);
    }

    public void addKeyBoardListener(KeyListener keyboardListener) {
        addKeyListener(keyboardListener);
        requestFocus();
    }

    public void windowResized() {
        float scale = Math.min(WindowSettings.scaleX, WindowSettings.scaleY);
    }

    public ChamberView getChamberView() {
        return chamberView;
    }

}