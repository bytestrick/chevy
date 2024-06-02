package chevy.control;

import chevy.model.GameModel;
import chevy.view.GameView;

import java.awt.event.KeyListener;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;

    private final KeyboardListener keyboardListener;


    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.keyboardListener = new KeyboardListener(gameView);

        this.gameView.getGamePanel().setChamber(gameModel.getCurrentChamber());
        initChamberController();
    }

    private void initChamberController() {
        ChamberController chamberController = new ChamberController(gameModel.getCurrentChamber());
        keyboardListener.setChamber(chamberController);
    }
}
