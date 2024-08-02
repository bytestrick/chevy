package chevy.view;

import chevy.model.chamber.Chamber;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.settings.GameSettings;
import chevy.settings.WindowSettings;
import chevy.view.chamber.ChamberView;
import chevy.view.entityView.entityViewAnimated.collectable.powerUp.PowerUpTextVisualizerView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class GamePanel extends JPanel implements Render {
    private final ChamberView chamberView;
    private final PowerUpTextVisualizerView powerUpTextVisualizerView;


    public GamePanel() {
        this.chamberView = new ChamberView();
        this.powerUpTextVisualizerView = new PowerUpTextVisualizerView();

        setLayout(new BorderLayout());

        powerUpTextVisualizerView.setVisible(false);
        add(powerUpTextVisualizerView, BorderLayout.CENTER);

        RenderManager.addToRender(this);
        setBackground(Color.BLACK);
    }


    public void addKeyBoardListener(KeyListener keyboardListener) {
        addKeyListener(keyboardListener);
        requestFocus();
    }

    public void windowResized() {
        float scale = Math.min(WindowSettings.scaleX, WindowSettings.scaleY);
        powerUpTextVisualizerView.resizeFont(scale);
    }

    public ChamberView getChamberView() {
        return chamberView;
    }

    public PowerUpTextVisualizerView getPowerUpTextVisualizerController() {
        return powerUpTextVisualizerView;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        chamberView.draw(g);
    }

    @Override
    public void render(double delta) {
        repaint();
    }
}