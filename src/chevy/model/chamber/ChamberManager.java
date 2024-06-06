package chevy.model.chamber;

import java.util.ArrayList;
import java.util.List;

public class ChamberManager {
    private static ChamberManager instance = null;
    List<Chamber> chambers;
    private int currentNChamber;


    private ChamberManager() {
        this.chambers = new ArrayList<>();
        this.currentNChamber = 0;
    }


    public static ChamberManager getInstance() {
        if (instance == null)
            instance = new ChamberManager();
        return instance;
    }

    // ---

    private boolean createChamber(int n) {
        if (n < chambers.size())
            return true;

        boolean loaded = false;
        Chamber chamber = new Chamber();
        loaded = LoadChamber.loadChamber(n, chamber);
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
