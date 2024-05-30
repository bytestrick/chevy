package chevy.model.chamber;

import java.util.ArrayList;
import java.util.List;

public class ChamberManager {
    List<Chamber> chambers = new ArrayList<>();
    private static ChamberManager instance = null;
    private static int currentNChamber = 0;


    private ChamberManager() {}


    public static ChamberManager getInstance() {
        if (instance == null)
            instance = new ChamberManager();
        return instance;
    }

    private boolean createChamber(int n) {
        boolean loaded = false;
        Chamber chamber = new Chamber();
        loaded = LoadChamber.loadChamber(n, chamber);
        chamber.show();
        chambers.add(chamber);
        return loaded;
    }

    public Chamber getNextChamber() {
        Chamber chamber = null;
        if (createChamber(currentNChamber + 1)) {
            ++currentNChamber;
            chamber = chambers.get(currentNChamber);
        }
        return chamber;
    }

    public Chamber getCurrentChamber() {
        Chamber chamber = null;
        if (createChamber(currentNChamber))
            chamber = chambers.get(currentNChamber);
        return chamber;
    }
}
