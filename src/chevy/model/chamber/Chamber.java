package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.player.Player;
import chevy.utilz.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chamber {
    private List<List<List<Entity>>> chamber;
    private int nRow;
    private int nCol;
    private boolean init = false;
    private Player player;


    public Chamber() {}


    public void initWorld(int nRow, int nCol) {
        this.nRow = nRow;
        this.nCol = nCol;

        chamber = new ArrayList<>(nRow);
        for (int i = 0; i < nRow; ++i) {
            List<List<Entity>> row = new ArrayList<>(nCol);
            for (int j = 0; j < nCol; ++j)
                row.add(new ArrayList<>());
            chamber.add(row);
        }
        init = true;
    }

    // ----------

    private boolean validatePosition(Vector2<Integer> vector2) {
        if (!init)
            return false;
        return vector2.first() >= 0 && vector2.first() < nRow
                && vector2.second() >= 0 && vector2.second() < nCol;
    }

    private boolean canCross(Vector2<Integer> vector2) {
        return validatePosition(vector2) &&
                getEntityOnTop(vector2).getCrossable();
    }

    public void movePlayer(DirectionsModel direction) {
        Vector2<Integer> nextVelocity = new Vector2<>(
                player.getRow() + direction.col(),
                player.getCol() + direction.row()
        );
        if (canCross(nextVelocity)) {
            removeEntityOnTop(player);
            player.changePosition(nextVelocity);
            addEntityOnTop(player);
        }
        show();
    }

    // ------------

    public void removeEntityOnTop(Entity entity) { chamber.get(entity.getRow()).get(entity.getCol()).removeLast(); }

    public void addEntityOnTop(Entity entity) { chamber.get(entity.getRow()).get(entity.getCol()).add(entity); }

    public Entity getEntityOnTop(int row, int col) { return chamber.get(row).get(col).getLast(); }

    public Entity getEntityOnTop(List<Entity> entities) { return entities.getLast(); }

    public Entity getEntityOnTop(Vector2<Integer> vector2) { return chamber.get(vector2.first()).get(vector2.second()).getLast(); }

    public List<List<List<Entity>>> getChamber() { return Collections.unmodifiableList(chamber); }

    public boolean isInitialized() { return init; }

    public void show() {
        for (List<List<Entity>> r : chamber) {
            for (List<Entity> c : r) {
                Entity onTop = getEntityOnTop(c);
                if (onTop != null)
                    System.out.print(" | " + onTop);
                else {
                    System.out.print(" | NULL");
                }
            }
            System.out.println(" |");
        }
    }

    public void setPlayer(Player player) { this.player = player; }

    public Player getPlayer() { return this.player; }
}
