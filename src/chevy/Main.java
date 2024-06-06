package chevy;

import chevy.control.GameController;
import chevy.model.GameModel;
import chevy.model.chamber.ChamberManager;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.service.GameLoop;
import chevy.settings.GameSettings;
import chevy.utilz.Utilz;
import chevy.utilz.Vector2;
import chevy.view.GameView;

public class Main {
    public static void main(String[] args) {
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView();

        new GameController(gameModel, gameView);
        new GameLoop();
    }
}