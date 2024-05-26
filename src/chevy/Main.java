package chevy;

import chevy.model.world.ChamberManager;

public class Main {
    public static void main(String[] args) {
        ChamberManager chamberManager = ChamberManager.getInstance();
        chamberManager.addChamber(1);
    }
}