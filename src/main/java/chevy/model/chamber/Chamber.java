package chevy.model.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.chamber.drawOrder.LayerManager;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Coin;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.Key;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.collectable.powerUp.SlimePiece;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utils.Log;
import chevy.utils.Utils;
import chevy.view.chamber.ChamberView;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * The game area
 */
public final class Chamber {
    /**
     * Enemies in the room
     */
    private final List<Enemy> enemies = new LinkedList<>();
    /**
     * Traps in the room
     */
    private final List<Trap> traps = new LinkedList<>();
    /**
     * Projectiles in the room
     */
    private final List<Projectile> projectiles = new LinkedList<>();
    /**
     * Collectables in the room
     */
    private final List<Collectable> collectables = new LinkedList<>();
    /**
     * Dynamic elements of the environment (chests, ladders)
     */
    private final List<Environment> environments = new LinkedList<>();
    /**
     * Order in which entities are drawn
     */
    private final LayerManager drawOrderChamber = new LayerManager();
    /**
     * A three-dimensions data structure representing the game grid.
     * Every cell in the grid can contain a list of entities.
     */
    private List<List<List<Entity>>> chamber;
    /**
     * Rows and columns of the game grid
     */
    private Dimension size;
    /**
     * Indicates if the world has been initialized
     */
    private boolean initialized;
    /**
     * The current player
     */
    private Player player;
    private int enemyCounter;

    /**
     * @param size number of columns and rows of the game grid
     */
    void initWorld(Dimension size) {
        this.size = size;
        ChamberView.updateSize(size);

        chamber = new ArrayList<>(size.height);
        for (int i = 0; i < size.height; ++i) {
            List<List<Entity>> row = new LinkedList<>();
            for (int j = 0; j < size.width; ++j) {
                row.add(new LinkedList<>());
            }
            chamber.add(row);
        }

        initialized = true;
    }

    /**
     * Check if a position is valid inside the grid.
     *
     * @param position the position to check
     * @return {@code true} if it's valid, {@code false} otherwise
     */
    boolean isValid(Point position) {
        return isValid(position.y, position.x);
    }

    /**
     * Check if a position is valid inside the grid.
     *
     * @param row    the row of the position
     * @param column the column of the position
     * @return {@code true} if it's valid, {@code false} otherwise
     */
    private boolean isValid(int row, int column) {
        if (initialized) {
            return row >= 0 && row < size.height && column >= 0 && column < size.width;
        }
        return false;
    }

    /**
     * Check if a cell can be crossed by a {@link DynamicEntity}
     *
     * @param dynamicEntity the entity that wants to cross the cell
     * @param direction     direction in which the entity wants to move
     * @return {@code true} if it can be crossed, {@code false} otherwise
     */
    public synchronized boolean canCross(DynamicEntity dynamicEntity, Direction direction) {
        return canCross(direction.advance(dynamicEntity.getPosition()));
    }

    /**
     * Check if a cell can be crossed
     *
     * @param position the position of the cell to check
     * @return {@code true} if it can be crossed, {@code false} otherwise
     */
    private synchronized boolean canCross(Point position) {
        if (isValid(position)) {
            return chamber.get(position.y).get(position.x).stream().allMatch(Entity::isCrossable);
        }
        return false;
    }

    /**
     * Check if a position is safe to cross (used mainly to prevent enemies from moving on cells where they could take damage).
     *
     * @param dynamicEntity dynamic entity to consider
     * @param direction     direction in which the entity wants to move
     * @return {@code true} if the movement won't damage the dynamic entity, {@code false} otherwise
     */
    private boolean isSafeToCross(DynamicEntity dynamicEntity, Direction direction) {
        return isSafeToCross(direction.advance(dynamicEntity.getPosition()));
    }

    /**
     * Check if a position is safe to cross (used mainly to prevent enemies from moving on cells where they could take damage).
     *
     * @param position position to consider
     * @return {@code true} if the movement won't damage the dynamic entity, {@code false} otherwise
     */
    boolean isSafeToCross(Point position) {
        Entity onTop = getEntityOnTop(position);
        return canCross(position) && onTop != null && onTop.isSafeToCross();
    }

    /**
     * @param dynamicEntity dynamic entity to consider
     * @param direction     direction in which the dynamic entity wants to move
     * @return entity on top of the grid in the cell where the player wants to move
     */
    public synchronized Entity getEntityNearOnTop(DynamicEntity dynamicEntity,
                                                  Direction direction) {
        return getEntityNearOnTop(dynamicEntity, direction, 1);
    }

    /**
     * @param dynamicEntity dynamic entity to consider
     * @param direction     direction in which the dynamic entity wants to move
     * @param distanceCell  offset that considers the cells further ahead than the selected direction
     * @return entity on top of the grid in the cell where the player wants to move
     */
    public synchronized Entity getEntityNearOnTop(DynamicEntity dynamicEntity,
                                                  Direction direction, int distanceCell) {
        if (direction == null || dynamicEntity == null) {
            return null;
        }

        Point position = dynamicEntity.getPosition();
        position.translate(direction.x * distanceCell, direction.y * distanceCell);
        if (isValid(position)) {
            return getEntityOnTop(position);
        }

        return null;
    }

    /**
     * Check if an entity can be spawned in a specific cell.
     *
     * @param position position to consider
     * @return {@code true} if it can be created, {@code false} otherwise
     */
    private synchronized boolean canSpawn(Point position) {
        final Entity onTop = getEntityOnTop(position);
        return isValid(position) && onTop != null && onTop.isCrossable() && !(onTop instanceof Void);
    }

    /**
     * Given an entity, it returns the direction in which it should move to hit the player.
     *
     * @param entity entity to consider for the direction calculation
     * @return the direction in which the player is
     */
    public synchronized Direction getDirectionToHitPlayer(Entity entity) {
        return getDirectionToHitPlayer(entity, 1);
    }

    /**
     * Given an entity, it returns the direction in which it should move to hit the player.
     *
     * @param entity       entity to consider for the direction calculation
     * @param distanceCell offset that considers the cells further ahead than the selected direction
     * @return the direction in which the player is
     */
    public synchronized Direction getDirectionToHitPlayer(Entity entity, int distanceCell) {
        for (int i = 1; i <= distanceCell; ++i) {
            for (Direction direction : Direction.values()) {
                Point position = entity.getPosition();
                position.x += direction.x * i;
                position.y += direction.y * i;

                if (isValid(position) && getEntityOnTop(position) instanceof Player) {
                    if (entity instanceof DynamicEntity dynamicEntity) {
                        dynamicEntity.setDirection(direction);
                    }
                    return direction;
                }
            }
        }
        return null;
    }

    /**
     * Move a dynamic entity to the next cell in the selected direction.
     *
     * @param dynamicEntity dynamic entity to move
     * @param direction     direction in which the entity wants to move
     */
    public synchronized void moveDynamicEntity(DynamicEntity dynamicEntity,
                                               Direction direction) {
        Point nextPosition = direction.advance(dynamicEntity.getPosition());
        if (canCross(nextPosition)) {
            dynamicEntity.setDirection(direction);

            if (findAndRemoveEntity(dynamicEntity)) {
                dynamicEntity.setPosition(nextPosition);
                addEntityOnTop(dynamicEntity);
            }
        }
    }

    /**
     * Move a dynamic entity to the next cell in the selected direction.
     *
     * @param dynamicEntity dynamic entity to move
     * @param nextPosition  position where the entity wants to move
     */
    private synchronized void moveDynamicEntity(DynamicEntity dynamicEntity,
                                                Point nextPosition) {
        Direction direction = Direction.positionToDirection(dynamicEntity.getPosition(),
                nextPosition);

        if (canCross(nextPosition) && direction != null) {
            moveDynamicEntity(dynamicEntity, direction);
        }
    }

    /**
     * Find and remove an entity from the game grid.
     *
     * @param entity entity to remove
     * @return {@code true} if it was removed, {@code false} otherwise
     */
    public synchronized boolean findAndRemoveEntity(Entity entity) {
        return findAndRemoveEntity(entity, true);
    }

    /**
     * Find and remove an entity from the game grid.
     *
     * @param entity    entity to remove
     * @param setToDraw if {@code true}, the entity will be set to draw
     * @return {@code true} if it was removed, {@code false} otherwise
     */
    public synchronized boolean findAndRemoveEntity(Entity entity, boolean setToDraw) {
        List<Entity> entities = chamber.get(entity.getRow()).get(entity.getCol());
        ListIterator<Entity> it = entities.listIterator(entities.size());
        while (it.hasPrevious()) {
            Entity entity2 = it.previous();
            if (entity.equals(entity2)) {
                entity.setShouldDraw(setToDraw);
                it.remove();

                return true;
            }
        }
        return false;
    }

    /**
     * Spawn a specified number of {@link Slime} around an entity.
     *
     * @param entity considered entity
     * @param nSlime number of slimes to spawn
     */
    public synchronized void spawnSlimeAroundEntity(Entity entity, int nSlime) {
        int f = (int) System.currentTimeMillis(); // factor that randomizes the position

        for (int i = 0; i < 3 && nSlime > 0; ++i) {
            for (int j = 0; j < 3 && nSlime > 0; ++j) {
                int randomI = Utils.wrap(f + i, -1, 1);
                int randomJ = Utils.wrap(f + j, -1, 1);

                if (randomI != 0 || randomJ != 0) {
                    Point position = entity.getPosition();
                    position.translate(randomJ, randomI);
                    if (canSpawn(position)) {
                        Slime slime = new Slime(position);
                        addEnemy(slime);
                        addEntityOnTop(slime);
                        --nSlime;
                    }
                }
            }
        }
    }

    public synchronized void spawnCollectable(Enemy enemy) {
        if (!(getEntityOnTop(enemy.getPosition()) instanceof Sludge) && Enemy.canDrop()) {
            Collectable.Type collectableType = Enemy.getDrop();
            if (collectableType == null) {
                return;
            }
            switch (collectableType) {
                case COIN -> {
                    Coin coin = new Coin(enemy.getPosition());
                    addEntityOnTop(coin);
                    addCollectable(coin);
                }
                case HEALTH -> {
                    Health health = new Health(enemy.getPosition());
                    addEntityOnTop(health);
                    addCollectable(health);
                }
                case KEY -> {
                    Key key = new Key(enemy.getPosition());
                    addEntityOnTop(key);
                    addCollectable(key);
                }
            }
        }
    }

    public synchronized void spawnSlime(Enemy enemy) {
        SlimePiece slimePiece = (SlimePiece) player.getOwnedPowerUp(PowerUp.Type.SLIME_PIECE);
        if (slimePiece != null && slimePiece.canUse()) {
            spawnSlimeAroundEntity(enemy, SlimePiece.getNumberOfSlimes());
        }
    }

    public void spawnCollectableAroundChest(Chest chest, int nSpawn) {
        int f = (int) System.currentTimeMillis(); // factor that randomizes the position

        for (int i = 0; i < 3 && nSpawn > 0; ++i) {
            for (int j = 0; j < 3 && nSpawn > 0; ++j) {
                int randomI = Utils.wrap(f + i, -1, 1);
                int randomJ = Utils.wrap(f + j, -1, 1);

                if (randomI != 0 || randomJ != 0) {
                    Point position = chest.getPosition();
                    position.translate(randomJ, randomI);
                    if (canSpawn(position) && !(getEntityOnTop(position) instanceof Sludge)) {
                        Collectable.Type collectableType = chest.getDrop();
                        if (collectableType == null) {
                            return;
                        }
                        switch (collectableType) {
                            case COIN -> {
                                Coin coin = new Coin(position);
                                addEntityOnTop(coin);
                                addCollectable(coin);
                            }
                            case HEALTH -> {
                                Health health = new Health(position);
                                addEntityOnTop(health);
                                addCollectable(health);
                            }
                            case KEY -> {
                                Key key = new Key(position);
                                addEntityOnTop(key);
                                addCollectable(key);
                            }
                            case POWER_UP -> {
                                PowerUp powerUp = PowerUp.getPowerUp(position);
                                addEntityOnTop(powerUp);
                                addCollectable(powerUp);
                            }
                        }
                        --nSpawn;
                    }
                }
            }
        }
    }

    /**
     * Search for the player in a square range around an entity.
     *
     * @param entity entity to consider
     * @param edge   side of the square
     * @return the player if found, {@code null} otherwise
     */
    private synchronized Player findPlayerInSquareRange(Entity entity, int edge) {
        int startRow = entity.getRow() - edge;
        int startCol = entity.getCol() - edge;
        int endRow = entity.getRow() + edge;
        int endCol = entity.getCol() + edge;

        for (int i = startRow; i <= endRow; ++i) {
            for (int j = startCol; j <= endCol; ++j) {
                if (isValid(i, j) && getEntityOnTop(i, j) instanceof Player p) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Move the enemy randomly. The movement is not recalculated if the previous movement is invalid, for example:
     * if the function calculates that the enemy must move to the right where there is a wall, the movement is
     * discarded and the enemy will not move.
     *
     * @param enemy enemy to move
     * @return {@code true} if the enemy moved, {@code false} otherwise
     */
    public synchronized boolean moveRandom(Enemy enemy) {
        Direction directionMove = Direction.getRandom();
        if (isSafeToCross(enemy, directionMove)) {
            moveDynamicEntity(enemy, directionMove);
            return true;
        }
        return false;
    }

    /**
     * Move the enemy randomly. The movement is recalculated if the previous movement is invalid.
     * The operation is repeated until there are no more available directions or the new movement is valid.
     *
     * @param enemy enemy to move
     * @return {@code true} if the enemy moved, {@code false} otherwise
     */
    public synchronized boolean moveRandomPlus(Enemy enemy) {
        Direction[] directions = Direction.values();
        int index = Utils.random.nextInt(directions.length);
        for (int i = 0; i <= directions.length; ++i) {
            if (isSafeToCross(enemy, directions[index])) {
                moveDynamicEntity(enemy, directions[index]);
                return true;
            } else {
                index = Utils.wrap(index + i, 0, directions.length - 1);
            }
        }
        return false;
    }

    /**
     * Move the enemy randomly if the player is outside the square area, otherwise follow the shortest path to reach the player.
     * Uses {@link #moveRandom(Enemy)} for random movement.
     *
     * @param enemy       enemy to move
     * @param rangeWander side of the square area
     * @return {@code true} if it moves, {@code false} otherwise
     */
    public synchronized boolean wanderChase(Enemy enemy, int rangeWander) {
        // move towards the player if it is inside your field of view
        Player player = findPlayerInSquareRange(enemy, rangeWander);
        if (player != null) {
            return chase(enemy);
        }
        return moveRandom(enemy);
    }

    /**
     * Move the enemy randomly if the player is outside the square area, otherwise follow the shortest path to reach the player.
     * Uses {@link #moveRandomPlus(Enemy)} for random movement.
     *
     * @param enemy       enemy to move
     * @param rangeWander side of the square area
     * @return {@code true} if it moves, {@code false} otherwise
     */
    public synchronized boolean wanderChasePlus(Enemy enemy, int rangeWander) {
        // move towards the player if it is inside your field of view
        Player player = findPlayerInSquareRange(enemy, rangeWander);
        if (player != null) {
            return chase(enemy);
        }
        // move randomly
        return moveRandomPlus(enemy);
    }

    /**
     * Moves the enemy following the shortest path to reach the player
     *
     * @param enemy enemy to move
     * @return {@code true} if it moves, {@code false} otherwise
     */
    public synchronized boolean chase(Enemy enemy) {
        AStar aStar = new AStar(this);
        List<Point> path = aStar.find(enemy.getPosition(), player.getPosition());
        if (path != null) {
            if (isSafeToCross(path.get(1))) {
                moveDynamicEntity(enemy, path.get(1));
                return true;
            }
        }
        return moveRandom(enemy);
    }

    public synchronized void removeEntityOnTop(Entity entity) {
        entity.setShouldDraw(false);
        List<Entity> stack = chamber.get(entity.getRow()).get(entity.getCol());
        try {
            stack.removeLast();
        } catch (NoSuchElementException e) {
            Log.warn("Chamber.removeEntityOnTop: the cell is empty.");
        }
    }

    public synchronized void addEntityOnTop(Entity entity) {
        chamber.get(entity.getRow()).get(entity.getCol()).addLast(entity);
        if (entity.shouldNotDraw()) {
            entity.setShouldDraw(true);
            addEntityToDraw(entity, entity.getDrawLayer());
        }
    }

    private synchronized Entity getEntityOnTop(int row, int col) {
        return chamber.get(row).get(col).getLast();
    }

    public synchronized Entity getEntityOnTop(Point position) {
        try {
            return chamber.get(position.y).get(position.x).getLast();
        } catch (NoSuchElementException e) {
            Log.warn("Attempt to access cell (" + position.y + ", " + position.x + ") failed because it is empty");
        }
        return null;
    }

    public synchronized Entity getEntityBelowTheTop(Entity entity) {
        List<Entity> entities = chamber.get(entity.getRow()).get(entity.getCol());
        int index = entities.size() - 2;
        return index >= 0 ? entities.get(index) : null;
    }

    void addEnemy(Enemy enemy) {
        enemies.add(enemy);
        ++enemyCounter;
    }

    public void decreaseEnemyCounter() {
        if (--enemyCounter < 0) {
            ++enemyCounter;
        }
    }

    boolean isInitialized() {
        return initialized;
    }

    public Dimension getSize() {
        return size;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getEnemyCounter() {
        return enemyCounter;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    void addTraps(Trap trap) {
        traps.add(trap);
    }

    public List<Trap> getTraps() {
        return traps;
    }

    public synchronized void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public synchronized List<Projectile> getProjectiles() {
        return projectiles;
    }

    synchronized void addCollectable(Collectable collectable) {
        collectables.add(collectable);
    }

    public synchronized List<Collectable> getCollectables() {
        return collectables;
    }

    void addEnvironment(Environment environment) {
        environments.add(environment);
    }

    public List<Environment> getEnvironments() {
        return environments;
    }

    private void addEntityToDraw(Entity entity, int layer) {
        synchronized (drawOrderChamber) {
            drawOrderChamber.add(entity, layer);
        }
    }

    public List<Layer> getDrawOrderChamber() {
        synchronized (drawOrderChamber) {
            return Collections.unmodifiableList(drawOrderChamber.getDrawOrder());
        }
    }
}
