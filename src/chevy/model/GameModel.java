package chevy.model;

import chevy.model.chamber.Chamber;
import chevy.model.chamber.ChamberManager;

public class GameModel {
    private final ChamberManager chamberManager = ChamberManager.getInstance();

    public GameModel() {}

    public Chamber getCurrentChamber() {
        return chamberManager.getCurrentChamber();
    }
}
