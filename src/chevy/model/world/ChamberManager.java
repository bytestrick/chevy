package chevy.model.world;

import java.util.ArrayList;
import java.util.List;

public class ChamberManager {
    List<Chamber> chambers = new ArrayList<>();
    private static ChamberManager instance = null;

    private ChamberManager() {}

    public static ChamberManager getInstance() {
        if (instance == null)
            instance = new ChamberManager();
        return instance;
    }

    public void addChamber(int nChamber) {
        Chamber chamber = new Chamber();
        LoadChamber.loadChamber(nChamber, chamber);
        chambers.add(chamber);
    }
}
