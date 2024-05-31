package chevy.view;

import chevy.Game;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Objects;

public class Window extends JFrame {
    public static final Dimension size = new Dimension(800, 800);
    public static GamePanel gamePanel = null;
    public final Menu menu;
    private final Game game;
    public Scene scene;
    public static Font font;

    public Window(Game game) {
        this.game = game;

        loadFont();

        gamePanel = new GamePanel(game, this);
        menu = new Menu(this);

        setTitle("Chevy");
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(size);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
        setScene(Scene.MENU);
        setVisible(true);
        requestFocus();
    }

    private void loadFont() {
        try {
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Window.class.getResourceAsStream("/res/m6x11.ttf"))));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        font = new Font("m6x11", Font.PLAIN, 20);
    }

    public void setScene(Scene scene) {
        if (this.scene != scene) {
            System.out.println("Changing scene from " + this.scene + " to " + scene);
            this.scene = scene;
            setContentPane(switch (scene) {
                case MENU -> menu.root;
                case PLAYING -> gamePanel;
                case SETTINGS -> null;
                case OVER -> null;
                case FINISH -> null;
            });
            validate();
        }
    }

    public enum Scene {MENU, PLAYING, SETTINGS, OVER, FINISH}
}