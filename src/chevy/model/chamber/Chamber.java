package chevy.model.chamber;

import chevy.model.entity.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chamber {
    private List<List<List<Entity>>> chamber;
    private boolean init = false;


    public Chamber() {}


    public void initWorld(int nRow, int nCol) {
        chamber = new ArrayList<>(nRow);
        for (int i = 0; i < nRow; ++i) {
            List<List<Entity>> row = new ArrayList<>(nCol);
            for (int j = 0; j < nCol; ++j)
                row.add(new ArrayList<>());
            chamber.add(row);
        }
        init = true;
    }

    public void addEntityOnTop(Entity entity, int row, int col) {
        chamber.get(row).get(col).add(entity);
    }

    public Entity getEntityOnTop(int row, int col) {
        return chamber.get(row).get(col).getLast();
    }

    public Entity getEntityOnTop(List<Entity> entities) {
        return entities.getLast();
    }

    public List<List<List<Entity>>> getChamber() { return Collections.unmodifiableList(chamber); }


    public boolean isInitialized() { return init; }

    public void show() {
        for (List<List<Entity>> r : chamber) {
            for (List<Entity> c : r) {
                Entity onTop = getEntityOnTop(c);
                if (onTop != null)
                    TileFromEntity.get(onTop);
//                    System.out.print(" | " + onTop);
                else {
                    System.out.println(" | NULL");
                }
            }
//            System.out.println(" |");
        }
    }
}
