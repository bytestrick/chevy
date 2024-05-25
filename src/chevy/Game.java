package chevy;

import chevy.model.dinamicEntity.player.Archer;
import chevy.model.dinamicEntity.player.Player;
import chevy.utilz.Vector2;
import chevy.view.GamePanel;
import chevy.view.Window;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game {
    private static final int FPS = 120;
    private final Window window;
    public Player player;

    // TODO: mostra FPS a schermo in modo condizionale

    private Game() {
        System.out.println("Starting");
        window = new Window();
        //window.add(new Menu(window));
        GamePanel gamePanel = new GamePanel(this);

        window.add(gamePanel);

        player = new Archer(new Vector2<>(5, 4));

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            window.getContentPane().repaint();
            Toolkit.getDefaultToolkit().sync();
        }, 0, TimeUnit.SECONDS.toNanos(1) / FPS, TimeUnit.NANOSECONDS);
    }

    public static void main(String[] args) {
        new Game();
    }
}