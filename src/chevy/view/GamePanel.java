package chevy.view;

import chevy.Game;
import chevy.control.GameController;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class GamePanel extends JPanel {
    private final Game game;
    private final Window window;
    private BufferedImage slime;

    public GamePanel(Game game, Window window) {
        this.game = game;
        this.window = window;
        slime = loadSprite("/res/slime/idle.png");

        setBackground(Color.DARK_GRAY);
        setSize(Window.size);

        window.addKeyListener(new GameController(game, window));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, Window.size.width, Window.size.height);

        g.drawImage(slime, 0, 0, null);
    }
}