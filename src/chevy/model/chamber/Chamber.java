package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.utilz.Utilz;
import chevy.utilz.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Chamber {
    private List<List<List<Entity>>> chamber;
    private int nRow;
    private int nCol;
    private boolean init = false;
    private Player player;
    private final List<Enemy> enemies = new LinkedList<>();


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

    private synchronized boolean validatePosition(Vector2<Integer> vector2) {
        if (!init)
            return false;
        return vector2.first() >= 0 && vector2.first() < nRow
                && vector2.second() >= 0 && vector2.second() < nCol;
    }

    public synchronized boolean canCross(Vector2<Integer> vector2) {
        return validatePosition(vector2) &&
                getEntityOnTop(vector2).isCrossable();
    }

    public void movePlayer(Vector2<Integer> nextPosition) {
            removeEntityOnTop(player);
            player.changePosition(nextPosition);
            addEntityOnTop(player);
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
                    if (canCross(spawnPosition)) {
                        addEntityOnTop(new Slime(spawnPosition));
                        --nSlime;
                        System.out.println(spawnPosition);
                    }
                }
            }

    }

    // ------------
    public synchronized void removeEnemyFormEnemies(Enemy enemy) {
        enemies.remove(enemy);
    }

    public synchronized void removeEntityOnTop(Entity entity) { chamber.get(entity.getRow()).get(entity.getCol()).removeLast(); }

    public synchronized void addEntityOnTop(Entity entity) { chamber.get(entity.getRow()).get(entity.getCol()).add(entity); }

    public synchronized Entity getEntityOnTop(int row, int col) { return chamber.get(row).get(col).getLast(); }

    public synchronized  Entity getEntityOnTop(List<Entity> entities) { return entities.getLast(); }

    public synchronized Entity getEntityOnTop(Vector2<Integer> vector2) { return chamber.get(vector2.first()).get(vector2.second()).getLast(); }

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

    public void setPlayer(Player player) { this.player = player; }
    public Player getPlayer() { return this.player; }

    public void addEnemy(Enemy enemy) { this.enemies.add(enemy); }
    public List<Enemy> getEnemy() { return this.enemies; }
}
