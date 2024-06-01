package chevy;

import chevy.control.GameController;
import chevy.model.GameModel;
import chevy.model.chamber.ChamberManager;
import chevy.utilz.Utilz;
import chevy.view.GameView;

public class Main {
    public static void main(String[] args) {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();
        new GameController(gameModel, gameView);
    }
}