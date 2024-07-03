package chevy.view;

import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.view.chamber.ChamberView;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Render {
    private final ChamberView chamberView;

    public GamePanel() {
        this.chamberView = new ChamberView();

        RenderManager.addToRender(this);
        setBackground(Color.BLACK);
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
    public void render(double delta) {
        repaint();
    }
}