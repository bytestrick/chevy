package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.Traps;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.settings.GameSettings;
import chevy.utilz.Utilz;
import chevy.utilz.Vector2;

import java.util.*;

public class Chamber {
    private List<List<List<Entity>>> chamber;
    private int nRows;
    private int nCols;
    private boolean init = false;
    private Player player;
    private final List<Enemy> enemies = new LinkedList<>();


    public Chamber() {}


    public void initWorld(int nRow, int nCol) {
        this.nRows = nRow;
        this.nCols = nCol;
        GameSettings.nTileH = nRow;
        GameSettings.nTileW = nCol;

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

    public boolean validatePosition(Vector2<Integer> vector2) {
        if (!init)
            return false;
        return vector2.first >= 0 && vector2.first < nRows
                && vector2.second >= 0 && vector2.second < nCols;
    }

    public boolean validatePosition(int row, int col) {
        if (!init)
            return false;
        return row >= 0 && row < nRows
                && col >= 0 && col < nCols;
    }

    public boolean canCross(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );

        return validatePosition(vector2) &&
                getEntityOnTop(vector2).isCrossable();
    }

    public boolean isSafeToCross(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );

        Entity onTop = getEntityOnTop(vector2);
        return onTop instanceof Traps;
    }

    public boolean canCross(Vector2<Integer> vector2) {
        return validatePosition(vector2) &&
                getEntityOnTop(vector2).isCrossable();
    }

    public Entity getNearEntity(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );
        if (validatePosition(vector2))
            return getEntityOnTop(vector2);
        return null;
    }

    private boolean canSpawn(Vector2<Integer> vector2) {
        Entity onTop = getEntityOnTop(vector2);
        return validatePosition(vector2) &&
                onTop.isCrossable() &&
                !(onTop instanceof Void);
    }

    public DirectionsModel getHitDirectionPlayer(Entity entity) {
        for (DirectionsModel direction : DirectionsModel.values()) {
            Vector2<Integer> checkPosition = new Vector2<>(
                    entity.getRow() + direction.row(),
                    entity.getCol() + direction.col()
            );
        if (validatePosition(checkPosition) && getEntityOnTop(checkPosition) instanceof Player)
            return direction;
        }
        return null;
    }

    public void moveDynamicEntity(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> nextPosition = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );

        removeEntityOnTop(dynamicEntity);
        dynamicEntity.changePosition(nextPosition);
        addEntityOnTop(dynamicEntity);
    }

    public void moveDynamicEntity(DynamicEntity dynamicEntity, Vector2<Integer> nextPosition) {
        DirectionsModel direction = DirectionsModel.directionToPosition(
                new Vector2<>(dynamicEntity.getRow(), dynamicEntity.getCol()),
                nextPosition
        );
        if (direction != null)
            moveDynamicEntity(dynamicEntity, direction);
    }

    public synchronized void spawnSlimeAroundEntity(Entity entity, int nSlime) {
        int f = (int) System.currentTimeMillis(); // fattore che randomizza la posizione

        for (int i = 0; i < 3 && nSlime > 0; ++i)
            for (int j = 0; j < 3 && nSlime > 0; ++j) {
                int randomI = Utilz.wrap(f + i, -1 , 1);
                int randomJ = Utilz.wrap(f + j, -1 , 1);

                if (randomI != 0 || randomJ != 0) {
                    Vector2<Integer> spawnPosition = new Vector2<>(
                            entity.getRow() + randomI,
                            entity.getCol() + randomJ
                    );
                    if (canSpawn(spawnPosition)) {
                        Slime slime = new Slime(spawnPosition);
                        addEnemyInEnemies(slime);
                        addEntityOnTop(slime);
                        --nSlime;
                    }
                }
            }

    }

    public synchronized Player findPlayerInSquareRange(Entity entity, int edge) {
        int startRow = entity.getRow() - edge;
        int startCol = entity.getCol() - edge;
        int endRow = entity.getRow() + edge;
        int endCol = entity.getCol() + edge;


        for (int i = startRow; i <= endRow; ++i)
            for (int j = startCol; j <= endCol; ++j)
                if (validatePosition(i, j) && getEntityOnTop(i, j) instanceof Player p)
                    return p;

        return null;
    }

    // ------------
    public synchronized void removeEnemyFormEnemies(Enemy enemy) {
        enemies.remove(enemy);
    }

    public synchronized void addEnemyInEnemies(Enemy enemy) {
        enemies.addLast(enemy);
    }

    public synchronized void removeEntityOnTop(Entity entity) { chamber.get(entity.getRow()).get(entity.getCol()).removeLast(); }

    public synchronized void addEntityOnTop(Entity entity) { chamber.get(entity.getRow()).get(entity.getCol()).addLast(entity); }

    public synchronized Entity getEntityOnTop(int row, int col) { return chamber.get(row).get(col).getLast(); }

    public synchronized  Entity getEntityOnTop(List<Entity> entities) { return entities.getLast(); }

    public synchronized Entity getEntityOnTop(Vector2<Integer> vector2) { return chamber.get(vector2.first).get(vector2.second).getLast(); }

    public synchronized Entity getEntityBelowTheTop(Entity entity) {
        List<Entity> entities = chamber.get(entity.getRow()).get(entity.getCol());
        return entities.get(entities.size() - 2);
    }

    public synchronized List<List<List<Entity>>> getChamber() { return Collections.unmodifiableList(chamber); }

    public boolean isInitialized() { return init; }

    public void show() {
        System.out.println();
        for (List<List<Entity>> r : chamber) {
            for (List<Entity> c : r) {
                Entity onTop = getEntityOnTop(c);
                if (onTop != null) {
                    String a = onTop.toString();
                    System.out.print(" | " + a.charAt(0) + a.charAt(1));
                }
                else {
                    System.out.print(" | NULL");
                }
            }
            System.out.println(" |");
        }
        System.out.println();

        System.out.println("Nemici rimanenti: " + enemies);
        System.out.println();
    }

    public int getNRows() {
        return nRows;
    }

    public int getNCols() {
        return nCols;
    }

    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return this.player; }

    public void addEnemy(Enemy enemy) { this.enemies.add(enemy); }
    public List<Enemy> getEnemies() { return this.enemies; }
}
