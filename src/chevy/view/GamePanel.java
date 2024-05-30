package chevy.view;

import chevy.control.KeyboardListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends JPanel {
    private static final Random random = new Random();
    private BufferedImage slime;
    private GameView gameView;

    public GamePanel(GameView gameView) {
        setBackground(Color.DARK_GRAY);
        slime = loadSprite("/res/assets/slime/idle.png");
        this.gameView = gameView;
    }

    public void addKeyBoardListener(KeyListener keyboardListener) {
        addKeyListener(keyboardListener);
        requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.setColor(Color.DARK_GRAY);
//        g.fillRect(0, 0, Window.size.width, Window.size.height);
//
//        g.drawImage(slime, 0, 0, null);
    }

    // Da spostare in un posto più opportuno
    public static BufferedImage loadSprite(String resPath) {
        BufferedImage img;
        try {
            img = ImageIO.read(Objects.requireNonNull(GamePanel.class.getResourceAsStream(resPath)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return img;
    }
}