package chevy.view;

import chevy.settings.WindowSettings;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel {
    private final ChamberView chamberView;
    private final HUDView hudView;

    public GamePanel() {
        this.chamberView = new ChamberView();
        this.hudView = new HUDView(1.5f);

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        springLayout.putConstraint(SpringLayout.NORTH, chamberView, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, chamberView, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, chamberView, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, chamberView, 0, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.NORTH, hudView, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, hudView, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, hudView, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, hudView, 0, SpringLayout.SOUTH, this);

        add(hudView);
        add(chamberView);
        setBackground(Color.BLACK);
    }

    public void addKeyBoardListener(KeyListener keyboardListener) {
        addKeyListener(keyboardListener);
        requestFocus();
    }

    public void windowResized() {
        float scale = Math.min(WindowSettings.scaleX, WindowSettings.scaleY);
        hudView.windowResized(scale);
    }

    public ChamberView getChamberView() {
        return chamberView;
    }

    public HUDView getHudView() {
        return hudView;
    }
}