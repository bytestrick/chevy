package chevy.view;

import chevy.model.entity.dinamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.service.RenderManager;
import chevy.utilz.Vector2;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameView {
    private final Window window;
    private final GamePanel gamePanel;

    // TODO: mostra FPS a schermo in modo condizionale

    public GameView() {
        System.out.println("Starting");

        window = new Window();
        gamePanel = new GamePanel();
        window.add(gamePanel);

        RenderManager.addToRender(gamePanel);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}