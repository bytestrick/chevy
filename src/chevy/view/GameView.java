package chevy.view;

import chevy.model.entity.dinamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.utilz.Vector2;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameView {
    private static final int FPS = 120;
    private final Window window;
    private final GamePanel gamePanel;
    public Player player;

    // TODO: mostra FPS a schermo in modo condizionale

    public GameView() {
        System.out.println("Starting");
        window = new Window();
        // window.add(new Menu(window));
        gamePanel = new GamePanel(this);
        window.add(gamePanel);

        player = new Archer(new Vector2<>(5, 4));

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            window.getContentPane().repaint();
            Toolkit.getDefaultToolkit().sync();
        }, 0, TimeUnit.SECONDS.toNanos(1) / FPS, TimeUnit.NANOSECONDS);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    //    public static void main(String[] args) {
//        new GameView();
//    }
}