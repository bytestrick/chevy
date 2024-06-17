package chevy.control;

import chevy.model.GameModel;
import chevy.view.GameView;

public class GameController {
    private final GameModel gameModel;
    private final GameView gameView;

    private final KeyboardListener keyboardListener;


    public GameController(GameModel gameModel, GameView gameView) {
        this.gameModel = gameModel;
        this.gameView = gameView;
        this.keyboardListener = new KeyboardListener(gameView);

        this.gameView.getWindow().getGamePanel().getChamberView().setDrawOrderChamber(gameModel.getCurrentChamber().getDrawOrderChamber());
        initChamberController();
    }

    private void initChamberController() {
        ChamberController chamberController = new ChamberController(gameModel.getCurrentChamber());
        keyboardListener.setChamber(chamberController);
    }
}
