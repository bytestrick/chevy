package chevy;

import chevy.model.dinamicEntity.player.Archer;
import chevy.model.dinamicEntity.player.Player;
import chevy.utilz.Vector2;
import chevy.view.Window;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Toolkit;

public class Game {
    public Player player;

    private static final int FPS = 120;
    private final Window window;
    private static ScheduledExecutorService gameLoop;

    // TODO: mostra FPS a schermo in modo condizionale

    private Game() {
        System.out.println("Starting");
        window = new Window(this);
        player = new Archer(new Vector2<>(5, 4));
    }

    public static void startGameLoop() {
        assert gameLoop == null;
        gameLoop = Executors.newSingleThreadScheduledExecutor();
        gameLoop.scheduleAtFixedRate(() -> {
            Window.gamePanel.repaint();
            Toolkit.getDefaultToolkit().sync();
        }, 0, TimeUnit.SECONDS.toNanos(1) / FPS, TimeUnit.NANOSECONDS);
    }

    public static void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.close();
            gameLoop = null;
        }
    }

    public static void main(String[] args) { new Game(); }
}