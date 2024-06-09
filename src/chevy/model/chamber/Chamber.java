package chevy.model.chamber;

import chevy.model.chamber.drawOrder.LayerManager;
import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Traps;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.model.pathFinding.AStar;
import chevy.settings.GameSettings;
import chevy.utilz.Utilz;
import chevy.utilz.Vector2;

import java.util.*;

public class Chamber {
    private List<List<List<Entity>>> chamber;
    private LayerManager drawOrderChamber;

    private int nRows;
    private int nCols;
    private boolean init = false;
    private Player player;
    private final List<Enemy> enemies = new LinkedList<>();
    private final List<Traps> traps = new LinkedList<>();
    private final List<Projectile> projectiles = new LinkedList<>();


    public Chamber() {}


    public void initWorld(int nRow, int nCol) {
        this.nRows = nRow;
        this.nCols = nCol;
        GameSettings.nTileH = nRow;
        GameSettings.nTileW = nCol;

        chamber = new ArrayList<>(nRow);
        for (int i = 0; i < nRow; ++i) {
            List<List<Entity>> row = new LinkedList<>();
            for (int j = 0; j < nCol; ++j)
                row.add(new LinkedList<>());
            chamber.add(row);
        }

        drawOrderChamber = new LayerManager();
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

    public boolean validatePosition(Entity entity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(
                entity.getRow() + direction.row(),
                entity.getCol() + direction.col()
        );

        return validatePosition(vector2);
    }

    public synchronized boolean canCross(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );

        return canCross(vector2);
    }

    public synchronized boolean canCross(Vector2<Integer> vector2) {
        if (validatePosition(vector2)) {
            List<Entity> entityList = chamber.get(vector2.first).get(vector2.second);
            for (Entity entity : entityList)
                if (!entity.isCrossable()) {
                    return false;
                }
        }
        return true;
    }

    public boolean isSafeToCross(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );
        return isSafeToCross(vector2);
    }

    public boolean isSafeToCross(Vector2<Integer> vector2) {
        Entity onTop = getEntityOnTop(vector2);
        return canCross(vector2) && onTop.isSafeToCross();
    }

    public synchronized Entity getNearEntityOnTop(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );
        if (validatePosition(vector2))
            return getEntityOnTop(vector2);
        return null;
    }

    private synchronized boolean canSpawn(Vector2<Integer> vector2) {
        Entity onTop = getEntityOnTop(vector2);
        return validatePosition(vector2) &&
                onTop.isCrossable() &&
                !(onTop instanceof Void);
    }

    public synchronized DirectionsModel getHitDirectionPlayer(Entity entity) {
        return getHitDirectionPlayer(entity, 1);
    }

    public synchronized DirectionsModel getHitDirectionPlayer(Entity entity, int n) {
        for (int i = 1; i <= n; ++i)
            for (DirectionsModel direction : DirectionsModel.values()) {
                Vector2<Integer> checkPosition = new Vector2<>(
                        entity.getRow() + direction.row() * i,
                        entity.getCol() + direction.col() * i
                );
                if (validatePosition(checkPosition) && getEntityOnTop(checkPosition) instanceof Player)
                    return direction;
            }
        return null;
    }

    public synchronized void moveDynamicEntity(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> nextPosition = new Vector2<>(
                dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col()
        );

        findAndRemoveEntity(dynamicEntity);
        dynamicEntity.changePosition(nextPosition);
        addEntityOnTop(dynamicEntity);
    }

    public synchronized void moveDynamicEntity(DynamicEntity dynamicEntity, Vector2<Integer> nextPosition) {
        DirectionsModel direction = DirectionsModel.directionToPosition(
                new Vector2<>(dynamicEntity.getRow(), dynamicEntity.getCol()),
                nextPosition
        );
        if (direction != null)
            moveDynamicEntity(dynamicEntity, direction);
    }

    public synchronized boolean findAndRemoveEntity(Entity entity) {
        List<Entity> entities = chamber.get(entity.getRow()).get(entity.getCol());
        ListIterator<Entity> it = entities.listIterator(entities.size());
        while (it.hasPrevious()) {
            Entity entity2 = it.previous();
            if (entity.equals(entity2)) {
                it.remove();
                entity2.setToDraw(false);
                return true;
            }
        }
        return false;
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

    public synchronized boolean moveRandom(Enemy enemy) {
        DirectionsModel directionMove = DirectionsModel.getRandom();
        if (isSafeToCross(enemy, directionMove)) {
            moveDynamicEntity(enemy, directionMove);
            return true;
        }
        return false;
    }

    public synchronized boolean moveRandomPlus(Enemy enemy) {
        DirectionsModel[] directions = DirectionsModel.values();
        Random random = new Random();
        int index = random.nextInt(directions.length);
        for (int i = 0; i <= directions.length; ++i) {
            if (isSafeToCross(enemy, directions[index])) {
                moveDynamicEntity(enemy, directions[index]);
                return true;
            }
            else
                index = Utilz.wrap(index + i, 0, directions.length - 1);
        }
        return false;
    }

    public synchronized boolean wanderChase(Enemy enemy, int rangeWander) {
        // muoviti verso il player se si trova dentro il tuo campo visivo
        Player player = findPlayerInSquareRange(enemy, rangeWander);
        if (player != null) {
            return chase(enemy);
        }
        // muoviti in modo casuale
        return moveRandom(enemy);
    }

    public synchronized boolean wanderChasePlus(Enemy enemy, int rangeWander) {
        // muoviti verso il player se si trova dentro il tuo campo visivo
        Player player = findPlayerInSquareRange(enemy, rangeWander);
        if (player != null) {
            return chase(enemy);
        }
        // muoviti in modo casuale
        return moveRandomPlus(enemy);
    }

    public synchronized boolean chase(Enemy enemy) {
        AStar aStar = new AStar(this);
        List<Vector2<Integer>> path = aStar.find(enemy, player);
        if (path != null) {
            if (isSafeToCross(path.get(1))) {
                moveDynamicEntity(enemy, path.get(1));
                return true;
            }
        }

        return moveRandom(enemy);
    }

    // ------------

    public synchronized void removeEntityOnTop(Entity entity) {
        chamber.get(entity.getRow()).get(entity.getCol()).removeLast();
        entity.setToDraw(false);
    }

    public synchronized void addEntityOnTop(Entity entity) {
        chamber.get(entity.getRow()).get(entity.getCol()).addLast(entity);
        entity.setToDraw(true);
        drawOrderChamber.add(entity, entity.getLayer());
    }

    public synchronized Entity getEntityOnTop(int row, int col) { return chamber.get(row).get(col).getLast(); }

    public synchronized  Entity getEntityOnTop(List<Entity> entities) { return entities.getLast(); }

    public synchronized Entity getEntityOnTop(Vector2<Integer> vector2) { return chamber.get(vector2.first).get(vector2.second).getLast(); }

    public synchronized Entity getEntityOnTop(Entity entity) {
        return getEntityOnTop(new Vector2<>(entity.getRow(), entity.getCol()));
    }

    public synchronized Entity getEntityBelowTheTop(Entity entity) {
        List<Entity> entities = chamber.get(entity.getRow()).get(entity.getCol());
        int index = entities.size() - 2;
        return index >= 0 ? entities.get(index) : null;
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
    public void addTraps(Traps trap) { traps.add(trap); }

    public List<Traps> getTraps() { return traps; }
    public synchronized void addProjectile(Projectile projectile) { projectiles.add(projectile); }

    public synchronized List<Projectile> getProjectiles() { return projectiles; }

    public synchronized void removeEnemyFormEnemies(Enemy enemy) { enemies.remove(enemy); }
    public synchronized void addEnemyInEnemies(Enemy enemy) { enemies.addLast(enemy); }

    // ---

    public void addEntityToDraw(Entity entity, int layer) {
        drawOrderChamber.add(entity, layer);
    }
    public List<Layer> getDrawOrderChamber() { return Collections.unmodifiableList(drawOrderChamber.getDrawOrder()); }
}
