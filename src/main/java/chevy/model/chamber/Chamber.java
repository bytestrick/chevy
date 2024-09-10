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
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utils.Log;
import chevy.utils.Utils;
import chevy.view.chamber.ChamberView;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * L'area di gioco
 */
public final class Chamber {
    /** Nemici nella stanza */
    private final List<Enemy> enemies = new LinkedList<>();
    /** Trappole nella stanza */
    private final List<Trap> traps = new LinkedList<>();
    /** Proiettili nella stanza */
    private final List<Projectile> projectiles = new LinkedList<>();
    /** Collezionabili nella stanza */
    private final List<Collectable> collectables = new LinkedList<>();
    /** Elementi dinamici dell'ambiente (casse, scale) */
    private final List<Environment> environments = new LinkedList<>();
    /** Ordine delle in cui le entità vanno disegnate */
    private final LayerManager drawOrderChamber = new LayerManager();
    /**
     * Una struttura dati tridimensionale che rappresenta la griglia di gioco.
     * Ogni cella della griglia può contenere una lista di entità.
     */
    private List<List<List<Entity>>> chamber;
    /** Righe e colonne della griglia di gioco */
    private Dimension size;
    /** Indica se il mondo è stato inizializzato */
    private boolean initialized;
    /** Il giocatore attuale */
    private Player player;
    private int enemyCounter;

    /**
     * @param size numero di colonne e righe della griglia di gioco
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
     * Verifica se una posizione è valida all'interno della griglia.
     *
     * @param position la posizione da validare
     * @return true se è valida, false altrimenti
     */
    boolean validatePosition(Point position) {
        return validatePosition(position.y, position.x);
    }

    private boolean validatePosition(int i, int j) {
        if (initialized) {
            return i >= 0 && i < size.height && j >= 0 && j < size.width;
        }
        return false;
    }

    /**
     * Verifica se una determinata cella in una determinata posizione può essere attraversata da
     * un'entità dinamica.
     *
     * @param dynamicEntity entità dinamica da considerare.
     * @param direction     direzione in cui l'entità si deve spostare.
     * @return true se si può passare, false altrimenti.
     */
    public synchronized boolean canCross(DynamicEntity dynamicEntity, Direction direction) {
        return canCross(direction.advance(dynamicEntity.getPosition()));
    }

    /**
     * Verifica se una determinata cella in una determinata posizione può essere attraversata.
     *
     * @param position posizione in cui è presente la cella da controllare
     * @return true se si può passare, false altrimenti
     */
    private synchronized boolean canCross(Point position) {
        if (validatePosition(position)) {
            List<Entity> entityList = chamber.get(position.y).get(position.x);
            for (Entity entity : entityList) {
                if (!entity.isCrossable()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Verifica se una posizione è sicura da attraversare
     * (usata principalmente per far si che i nemici non si spostano su celle in cui potrebbero
     * prendere danno).
     *
     * @param dynamicEntity entità dinamica considerata
     * @param direction     direzione in cui si deve spostare
     * @return true se lo spostamento non danneggerà l'entità dinamica, false altrimenti
     */
    private boolean isSafeToCross(DynamicEntity dynamicEntity, Direction direction) {
        return isSafeToCross(direction.advance(dynamicEntity.getPosition()));
    }

    /**
     * Verifica se una posizione è sicura da attraversare
     * (usata principalmente per far si che i nemici non si spostano su celle in cui potrebbero
     * prendere danno).
     *
     * @param position posizione della cella da controllare
     * @return true se lo spostamento non danneggerà l'entità dinamica, false altrimenti
     */
    boolean isSafeToCross(Point position) {
        Entity onTop = getEntityOnTop(position);
        return canCross(position) && onTop != null && onTop.isSafeToCross();
    }

    /**
     * Ritorna l'entità in cima alla griglia, situata nella cella in cui si dovrà spostare il
     * player.
     *
     * @param dynamicEntity entità dinamica da considerare
     * @param direction     direzione in cui si deve spostare l'entità dinamica
     * @return entità in cima alla griglia.
     */
    public synchronized Entity getEntityNearOnTop(DynamicEntity dynamicEntity,
                                                  Direction direction) {
        return getEntityNearOnTop(dynamicEntity, direction, 1);
    }

    /**
     * Ritorna l'entità in cima alla griglia, situata nella cella in cui si dovrà spostare il
     * player.
     *
     * @param dynamicEntity entità dinamica da considerare
     * @param direction     direzione in cui si deve spostare l'entità dinamica
     * @param distanceCell  offset che considera le celle più avanti rispetto alla direzione
     *                      selezionata
     * @return entità in cima alla griglia
     */
    public synchronized Entity getEntityNearOnTop(DynamicEntity dynamicEntity,
                                                  Direction direction, int distanceCell) {
        if (direction == null || dynamicEntity == null) {
            return null;
        }

        Point position = dynamicEntity.getPosition();
        position.translate(direction.x * distanceCell, direction.y * distanceCell);
        if (validatePosition(position)) {
            return getEntityOnTop(position);
        }

        return null;
    }

    /**
     * Controlla se un'entità può essere creata in una determinata cella.
     *
     * @param position posizione della cella da verificare
     * @return true se può essere creata, false altrimenti
     */
    private synchronized boolean canSpawn(Point position) {
        final Entity onTop = getEntityOnTop(position);
        return validatePosition(position) && onTop != null && onTop.isCrossable() && !(onTop instanceof Void);
    }

    /**
     * Data un entità ritorna la direzione in cui bisogna avanzare per incontrare il player.
     *
     * @param entity entità da considerare per il calcolo della direzione
     * @return direzione in cui si trova il player
     */
    public synchronized Direction getDirectionToHitPlayer(Entity entity) {
        return getDirectionToHitPlayer(entity, 1);
    }

    /**
     * Data un entità ritorna la direzione in cui bisogna avanzare per incontrare il player.
     *
     * @param entity       entità da considerare per il calcolo della direzione
     * @param distanceCell offset che considera le celle più avanti rispetto alla direzione
     *                     selezionata
     * @return direzione in cui si trova il player
     */
    public synchronized Direction getDirectionToHitPlayer(Entity entity, int distanceCell) {
        for (int i = 1; i <= distanceCell; ++i) {
            for (Direction direction : Direction.values()) {
                Point position = direction.advance(entity.getPosition());
                position.x *= i;
                position.y *= i;
                if (validatePosition(position) && getEntityOnTop(position) instanceof Player) {
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
     * Sposta un entità dinamica nella cella successiva in corrispondenza della direzione
     * selezionata.
     *
     * @param dynamicEntity entità dinamica da spostare
     * @param direction     direzione in cui l'entità dinamica si deve spostare
     */
    public synchronized void moveDynamicEntity(DynamicEntity dynamicEntity,
                                               Direction direction) {
        Point nextPosition = direction.advance(dynamicEntity.getPosition());
        if (canCross(nextPosition)) {
            dynamicEntity.setDirection(direction);

            if (findAndRemoveEntity(dynamicEntity)) {
                dynamicEntity.changePosition(nextPosition);
                addEntityOnTop(dynamicEntity);
            }
        }
    }

    /**
     * Sposta un entità dinamica alla cella presente nella posizione data.
     *
     * @param dynamicEntity entità dinamica da spostare
     * @param nextPosition  posizione in cui è presente la cella
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
     * Trova e rimuove un'entità dalla griglia di gioco.
     *
     * @param entity entità da rimuovere
     * @return true se è stata rimossa, false altrimenti
     */
    public synchronized boolean findAndRemoveEntity(Entity entity) {
        return findAndRemoveEntity(entity, true);
    }

    /**
     * Trova e rimuove un'entità dalla griglia di gioco.
     *
     * @param entity    entità da rimuovere
     * @param setToDraw booleana che in base al valore permette all'entità di essere mostrata a
     *                  schermo
     * @return true se è stata rimossa, false altrimenti
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
     * Genera un numero specificato di Slime attorno a un'entità.
     *
     * @param entity entità considerata
     * @param nSlime numero di slime da generare
     */
    public synchronized void spawnSlimeAroundEntity(Entity entity, int nSlime) {
        int f = (int) System.currentTimeMillis(); // fattore che randomizza la posizione

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
        if (Enemy.canDrop()) {
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
        int f = (int) System.currentTimeMillis(); // fattore che randomizza la posizione

        for (int i = 0; i < 3 && nSpawn > 0; ++i) {
            for (int j = 0; j < 3 && nSpawn > 0; ++j) {
                int randomI = Utils.wrap(f + i, -1, 1);
                int randomJ = Utils.wrap(f + j, -1, 1);

                if (randomI != 0 || randomJ != 0) {
                    Point position = chest.getPosition();
                    position.translate(randomJ, randomI);
                    if (canSpawn(position)) {
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
     * Cerca il giocatore in un intervallo quadrato attorno a un'entità.
     *
     * @param entity entità da considerare
     * @param edge   lato del quadrato
     * @return ritorna il giocatore se presente nell'area, null altrimenti
     */
    private synchronized Player findPlayerInSquareRange(Entity entity, int edge) {
        int startRow = entity.getRow() - edge;
        int startCol = entity.getCol() - edge;
        int endRow = entity.getRow() + edge;
        int endCol = entity.getCol() + edge;

        for (int i = startRow; i <= endRow; ++i) {
            for (int j = startCol; j <= endCol; ++j) {
                if (validatePosition(i, j) && getEntityOnTop(i, j) instanceof Player p) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Muove in modo casuale il nemico. Il movimento non viene ricalcolato se il movimento
     * precedente non è valido,
     * as esempio: se la funzione calcola che il nemico si deve spostare a destra dove è presente
     * un muro, il movimento
     * viene scartato e il nemico non si sposterà.
     *
     * @param enemy nemico da spostare
     * @return true se il nemico è stato spostato, false altrimenti
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
     * Muove in modo casuale il nemico. Il movimento viene ricalcolato se quello precedente è
     * invalido.
     * L'operazione viene ripetuta finché non si esauriscono le direzioni disponibili o il nuovo
     * movimento è valido.
     *
     * @param enemy nemico da spostare
     * @return true se il nemico è stato spostato, false altrimenti
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
     * Funzione usata per spostare il nemico in modo casuale se il giocatore si trova fuori
     * dall'area quadrata,
     * altrimenti segue il percorso minore per raggiungere il giocatore. Usa
     * {@link #moveRandom(Enemy)} per il
     * movimento random.
     *
     * @param enemy       nemico da spostare
     * @param rangeWander lato dell'area del quadrato
     * @return true se si sposta, false altrimenti
     */
    public synchronized boolean wanderChase(Enemy enemy, int rangeWander) {
        // muoviti verso il player se si trova dentro il tuo campo visivo
        Player player = findPlayerInSquareRange(enemy, rangeWander);
        if (player != null) {
            return chase(enemy);
        }
        return moveRandom(enemy);
    }

    /**
     * Funzione usata per spostare il nemico in modo casuale se il giocatore si trova fuori
     * dall'area quadrata,
     * altrimenti segue il percorso minore per raggiungere il giocatore. Usa
     * {@link #moveRandomPlus(Enemy)} per il
     * movimento random.
     *
     * @param enemy       nemico da spostare
     * @param rangeWander lato dell'area del quadrato
     * @return true se si sposta, false altrimenti
     */
    public synchronized boolean wanderChasePlus(Enemy enemy, int rangeWander) {
        // muoviti verso il player se si trova dentro il tuo campo visivo
        Player player = findPlayerInSquareRange(enemy, rangeWander);
        if (player != null) {
            return chase(enemy);
        }
        // muoviti in modo casuale
        return moveRandomPlus(enemy);
    }

    /**
     * Funzione usata per spostare seguendo il percorso minore per raggiungere il giocatore.
     *
     * @param enemy nemico da spostare.
     * @return true se si sposta, false altrimenti.
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
            Log.warn("Chamber.removeEntityOnTop: la cella è vuota.");
        }
    }

    public synchronized void addEntityOnTop(Entity entity) {
        chamber.get(entity.getRow()).get(entity.getCol()).addLast(entity);
        if (entity.shouldNotDraw()) {
            entity.setShouldDraw(true);
            addEntityToDraw(entity, entity.getDrawLayer());
        }
    }

    private synchronized Entity getEntityOnTop(int row, int col) {return chamber.get(row).get(col).getLast();}

    public synchronized Entity getEntityOnTop(Point position) {
        try {
            return chamber.get(position.y).get(position.x).getLast();
        } catch (NoSuchElementException e) {
            Log.warn("Tentativo di accedere alla cella (" + position.y + ", " + position.x + ") " +
                    "fallito perché è vuota");
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

    boolean isInitialized() {return initialized;}

    public Dimension getSize() {return size;}

    public Player getPlayer() {return player;}

    public void setPlayer(Player player) {this.player = player;}

    public int getEnemyCounter() {return enemyCounter;}

    public List<Enemy> getEnemies() {return enemies;}

    void addTraps(Trap trap) {traps.add(trap);}

    public List<Trap> getTraps() {return traps;}

    public synchronized void addProjectile(Projectile projectile) {projectiles.add(projectile);}

    public synchronized List<Projectile> getProjectiles() {return projectiles;}

    synchronized void addCollectable(Collectable collectable) {collectables.add(collectable);}

    public synchronized List<Collectable> getCollectables() {return collectables;}

    void addEnvironment(Environment environment) {environments.add(environment);}

    public List<Environment> getEnvironments() {return environments;}

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
