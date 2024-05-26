package chevy.model.world;

import chevy.model.staticEntity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Chamber {
    private List<List<Entity>> world;

    public Chamber() {}

    public void initWorld(int nRow, int nCol) {
        world = new ArrayList<>(nRow);
        List<Entity> row = new ArrayList<>(nCol);
        for (int i = 0; i < nRow; ++i)
            world.add(row);
    }

}
