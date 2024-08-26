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
import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.model.pathFinding.AStar;
import chevy.settings.GameSettings;
import chevy.utils.Log;
import chevy.utils.Utils;
import chevy.utils.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * L'area di gioco.
 * Gestisce l'inizializzazione della griglia di gioco, il posizionamento e il movimento delle entità.
 */
public class Chamber {
    private final List<Enemy> enemies = new LinkedList<>(); // Nemici nella stanza
    private final List<Trap> traps = new LinkedList<>(); // Trappole nella stanza
    private final List<Projectile> projectiles = new LinkedList<>(); // Proiettili nella stanza
    private final List<Collectable> collectables = new LinkedList<>(); // collezionabili nella stanza
    private final List<Environment> environments = new LinkedList<>(); // elementi dinamici dell'ambiente nella stanza (ceste, scale)
    /**
     * Una struttura dati tridimensionale che rappresenta la griglia di gioco.
     * Ogni cella della griglia può contenere una lista di entità.
     */
    private List<List<List<Entity>>> chamber;
    private LayerManager drawOrderChamber; // Ordine delle in cui le entità vanno disegnate
    private int nRows; // Righe della griglia di gioco
    private int nCols; // Colonne della griglia di gioco
    private boolean init = false; // Indica se il mondo è stato inizializzato
    private Player player; // Il giocatore attuale
    private int enemyCounter = 0;

    /**
     * Inizializza la griglia di gioco con le dimensioni specificate e aggiorna le impostazioni di gioco.
     *
     * @param nRow numero di righe che avrà la griglia
     * @param nCol numero di colonne che avrà la griglia
     */
    public void initWorld(int nRow, int nCol) {
        this.nRows = nRow;
        this.nCols = nCol;
        GameSettings.updateValue(nCol, nRow);

        chamber = new ArrayList<>(nRow);
        for (int i = 0; i < nRow; ++i) {
            List<List<Entity>> row = new LinkedList<>();
            for (int j = 0; j < nCol; ++j) {
                row.add(new LinkedList<>());
            }
            chamber.add(row);
        }

        drawOrderChamber = new LayerManager();
        init = true;
    }

    /**
     * Verifica se una posizione è valida all'interno della griglia.
     *
     * @param vector2 posizione da validare
     * @return true se valida, false altrimenti
     */
    public boolean validatePosition(Vector2<Integer> vector2) {
        if (init) {
            return vector2.first >= 0 && vector2.first < nRows && vector2.second >= 0 && vector2.second < nCols;
        }
        return false;
    }

    /**
     * Verifica se una posizione è valida all'interno della griglia.
     *
     * @param row riga da validare
     * @param col colonna da validare
     * @return true se è valida, false altrimenti
     */
    public boolean validatePosition(int row, int col) {
        if (init) {
            return row >= 0 && row < nRows && col >= 0 && col < nCols;
        }
        return false;
    }

    /**
     * Verifica se la posizione in cui si deve spostare l'entità è valida all'interno della griglia.
     *
     * @param entity    entità da considerare
     * @param direction direzione in cui l'entità si deve muovere
     * @return true se è valida, false altrimenti
     */
    public boolean validatePosition(Entity entity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(entity.getRow() + direction.row(), entity.getCol() + direction.col());

        return validatePosition(vector2);
    }

    /**
     * Verifica se una determinata cella in una determinata posizione può essere attraversata da un'entità dinamica.
     *
     * @param dynamicEntity entità dinamica da considerare.
     * @param direction     direzione in cui l'entità si deve spostare.
     * @return true se si può passare, false altrimenti.
     */
    public synchronized boolean canCross(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col());

        return canCross(vector2);
    }

    /**
     * Verifica se una determinata cella in una determinata posizione può essere attraversata.
     *
     * @param vector2 posizione in cui è presente la cella da controllare
     * @return true se si può passare, false altrimenti
     */
    public synchronized boolean canCross(Vector2<Integer> vector2) {
        if (validatePosition(vector2)) {
            List<Entity> entityList = chamber.get(vector2.first).get(vector2.second);
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
     * (usata principalmente per far si che i nemici non si spostano su celle in cui potrebbero prendere danno).
     *
     * @param dynamicEntity entità dinamica considerata
     * @param direction     direzione in cui si deve spostare
     * @return true se lo spostamento non danneggerà l'entità dinamica, false altrimenti
     */
    public boolean isSafeToCross(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> vector2 = new Vector2<>(dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col());
        return isSafeToCross(vector2);
    }

    /**
     * Verifica se una posizione è sicura da attraversare
     * (usata principalmente per far si che i nemici non si spostano su celle in cui potrebbero prendere danno).
     *
     * @param vector2 posizione della cella da controllare
     * @return true se lo spostamento non danneggerà l'entità dinamica, false altrimenti
     */
    public boolean isSafeToCross(Vector2<Integer> vector2) {
        Entity onTop = getEntityOnTop(vector2);
        return canCross(vector2) && onTop.isSafeToCross();
    }

    /**
     * Ritorna l'entità in cima alla griglia, situata nella cella in cui si dovrà spostare il player.
     *
     * @param dynamicEntity entità dinamica da considerare
     * @param direction     direzione in cui si deve spostare l'entità dinamica
     * @return entità in cima alla griglia.
     */
    public synchronized Entity getNearEntityOnTop(DynamicEntity dynamicEntity, DirectionsModel direction) {
        return getNearEntityOnTop(dynamicEntity, direction, 1);
    }

    /**
     * Ritorna l'entità in cima alla griglia, situata nella cella in cui si dovrà spostare il player.
     *
     * @param dynamicEntity entità dinamica da considerare
     * @param direction     direzione in cui si deve spostare l'entità dinamica
     * @param distanceCell  offset che considera le celle più avanti rispetto alla direzione selezionata
     * @return entità in cima alla griglia
     */
    public synchronized Entity getNearEntityOnTop(DynamicEntity dynamicEntity, DirectionsModel direction,
                                                  int distanceCell) {
        if (direction == null || dynamicEntity == null) {
            return null;
        }

        Vector2<Integer> vector2 = new Vector2<>(dynamicEntity.getRow() + direction.row() * distanceCell,
                dynamicEntity.getCol() + direction.col() * distanceCell);
        if (validatePosition(vector2)) {
            return getEntityOnTop(vector2);
        }

        return null;
    }

    /**
     * Controlla se un'entità può essere creata in una determinata cella.
     *
     * @param vector2 posizione della cella da verificare
     * @return true se può essere creata, false altrimenti
     */
    private synchronized boolean canSpawn(Vector2<Integer> vector2) {
        Entity onTop = getEntityOnTop(vector2);
        return validatePosition(vector2) && onTop.isCrossable() && !(onTop instanceof Void);
    }

    /**
     * Data un entità ritorna la direzione in cui bisogna avanzare per incontrare il player.
     *
     * @param entity entità da considerare per il calcolo della direzione
     * @return direzione in cui si trova il player
     */
    public synchronized DirectionsModel getHitDirectionPlayer(Entity entity) {
        return getHitDirectionPlayer(entity, 1);
    }

    /**
     * Data un entità ritorna la direzione in cui bisogna avanzare per incontrare il player.
     *
     * @param entity entità da considerare per il calcolo della direzione
     * @param distanceCell offset che considera le celle più avanti rispetto alla direzione selezionata
     * @return direzione in cui si trova il player
     */
    public synchronized DirectionsModel getHitDirectionPlayer(Entity entity, int distanceCell) {
        DirectionsModel[] directionsModel = DirectionsModel.values();

        for (int i = 1; i <= distanceCell; ++i) {
            for (DirectionsModel direction : directionsModel) {
                Vector2<Integer> checkPosition = new Vector2<>(entity.getRow() + direction.row() * i,
                        entity.getCol() + direction.col() * i);
                if (validatePosition(checkPosition) && getEntityOnTop(checkPosition) instanceof Player) {
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
     * Sposta un entità dinamica nella cella successiva in corrispondenza della direzione selezionata.
     *
     * @param dynamicEntity entità dinamica da spostare
     * @param direction direzione in cui l'entità dinamica si deve spostare
     */
    public synchronized void moveDynamicEntity(DynamicEntity dynamicEntity, DirectionsModel direction) {
        Vector2<Integer> nextPosition = new Vector2<>(dynamicEntity.getRow() + direction.row(),
                dynamicEntity.getCol() + direction.col());

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
     * @param nextPosition posizione in cui è presente la cella
     */
    public synchronized void moveDynamicEntity(DynamicEntity dynamicEntity, Vector2<Integer> nextPosition) {
        DirectionsModel direction = DirectionsModel.positionToDirection(new Vector2<>(dynamicEntity.getRow(),
                dynamicEntity.getCol()), nextPosition);

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
     * @param entity entità da rimuovere
     * @param setToDraw booleana che in base al valore permette all'entità di essere mostrata a schermo
     * @return true se è stata rimossa, false altrimenti
     */
    public synchronized boolean findAndRemoveEntity(Entity entity, boolean setToDraw) {
        List<Entity> entities = chamber.get(entity.getRow()).get(entity.getCol());
        ListIterator<Entity> it = entities.listIterator(entities.size());
        while (it.hasPrevious()) {
            Entity entity2 = it.previous();
            if (entity.equals(entity2)) {
                entity.setToDraw(setToDraw);
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
                    Vector2<Integer> spawnPosition = new Vector2<>(entity.getRow() + randomI,
                            entity.getCol() + randomJ);
                    if (canSpawn(spawnPosition)) {
                        Slime slime = new Slime(spawnPosition);
                        addEnemy(slime);
                        addEntityOnTop(slime);
                        --nSlime;
                    }
                }
            }
        }
    }

    // ---- power up

    public synchronized void spawnCollectable(Enemy enemy) {
        if (enemy.canDrop()) {
            Collectable.Type collectableType = enemy.getDrop();
            if (collectableType == null) {
                return;
            }
            switch (collectableType) {
                case COIN -> {
                    Coin coin = new Coin(new Vector2<>(enemy.getRow(), enemy.getCol()));
                    addEntityOnTop(coin);
                    addCollectable(coin);
                }
                case HEALTH -> {
                    Health health = new Health(new Vector2<>(enemy.getRow(), enemy.getCol()));
                    addEntityOnTop(health);
                    addCollectable(health);
                }
                case KEY -> {
                    Key key = new Key(new Vector2<>(enemy.getRow(), enemy.getCol()));
                    addEntityOnTop(key);
                    addCollectable(key);
                }
            }
        }
    }

    public synchronized void spawnSlime(Enemy enemy) {
        SlimePiece slimePiece = (SlimePiece) player.getOwnedPowerUp(PowerUp.Type.SLIME_PIECE);
        if (slimePiece != null && slimePiece.canUse()) {
            spawnSlimeAroundEntity(enemy, slimePiece.getNSlime());
        }
    }

    public void spawnCollectableAroundChest(Chest chest, int nSpawn) {
        int f = (int) System.currentTimeMillis(); // fattore che randomizza la posizione

        for (int i = 0; i < 3 && nSpawn > 0; ++i) {
            for (int j = 0; j < 3 && nSpawn > 0; ++j) {
                int randomI = Utils.wrap(f + i, -1, 1);
                int randomJ = Utils.wrap(f + j, -1, 1);

                if (randomI != 0 || randomJ != 0) {
                    Vector2<Integer> spawnPosition = new Vector2<>(chest.getRow() + randomI,
                            chest.getCol() + randomJ);
                    if (canSpawn(spawnPosition)) {
                        Collectable.Type collectableType = chest.getDrop();
                        if (collectableType == null) {
                            return;
                        }
                        switch (collectableType) {
                            case COIN -> {
                                Coin coin = new Coin(spawnPosition);
                                addEntityOnTop(coin);
                                addCollectable(coin);
                            }
                            case HEALTH -> {
                                Health health = new Health(spawnPosition);
                                addEntityOnTop(health);
                                addCollectable(health);
                            }
                            case KEY -> {
                                Key key = new Key(spawnPosition);
                                addEntityOnTop(key);
                                addCollectable(key);
                            }
                            case POWER_UP -> {
                                PowerUp powerUp = PowerUp.getPowerUp(spawnPosition);
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

    // ----

    /**
     * Cerca il giocatore in un intervallo quadrato attorno a un'entità.
     *
     * @param entity entità da considerare
     * @param edge   lato del quadrato
     * @return ritorna il giocatore se presente nell'area, null altrimenti
     */
    public synchronized Player findPlayerInSquareRange(Entity entity, int edge) {
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
     * Muove in modo casuale il nemico. Il movimento non viene ricalcolato se il movimento precedente non è valido,
     * as esempio: se la funzione calcola che il nemico si deve spostare a destra dove è presente un muro, il movimento
     * viene scartato e il nemico non si sposterà.
     *
     * @param enemy nemico da spostare
     * @return true se il nemico è stato spostato, false altrimenti
     */
    public synchronized boolean moveRandom(Enemy enemy) {
        DirectionsModel directionMove = DirectionsModel.getRandom();
        if (isSafeToCross(enemy, directionMove)) {
            moveDynamicEntity(enemy, directionMove);
            return true;
        }
        return false;
    }

    /**
     * Muove in modo casuale il nemico. Il movimento viene ricalcolato se quello precedente è invalido.
     * L'operazione viene ripetuta finché non si esauriscono le direzioni disponibili o il nuovo movimento è valido.
     *
     * @param enemy nemico da spostare
     * @return true se il nemico è stato spostato, false altrimenti
     */
    public synchronized boolean moveRandomPlus(Enemy enemy) {
        DirectionsModel[] directions = DirectionsModel.values();
        Random random = new Random();
        int index = random.nextInt(directions.length);
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
     * Funzione usata per spostare il nemico in modo casuale se il giocatore si trova fuori dall'area quadrata,
     * altrimenti segue il percorso minore per raggiungere il giocatore. Usa {@link #moveRandom(Enemy)} per il
     * movimento random.
     *
     * @param enemy nemico da spostare
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
     * Funzione usata per spostare il nemico in modo casuale se il giocatore si trova fuori dall'area quadrata,
     * altrimenti segue il percorso minore per raggiungere il giocatore. Usa {@link #moveRandomPlus(Enemy)} per il
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
        List<Vector2<Integer>> path = aStar.find(enemy, player);
        if (path != null) {
            if (isSafeToCross(path.get(1))) {
                moveDynamicEntity(enemy, path.get(1));
                return true;
            }
        }
        return moveRandom(enemy);
    }

    public synchronized void removeEntityOnTop(Entity entity) {
        entity.setToDraw(false);
        List<Entity> stack = chamber.get(entity.getRow()).get(entity.getCol());
        try {
            stack.removeLast();
        } catch (NoSuchElementException e) {
            Log.warn("Chamber.removeEntityOnTop: la cella è vuota.");
        }
    }

    public synchronized void addEntityOnTop(Entity entity) {
        chamber.get(entity.getRow()).get(entity.getCol()).addLast(entity);
        if (!entity.toDraw()) {
            entity.setToDraw(true);
            addEntityToDraw(entity, entity.getDrawLayer());
        }
    }

    public synchronized Entity getEntityOnTop(int row, int col) { return chamber.get(row).get(col).getLast(); }

    public synchronized Entity getEntityOnTop(Vector2<Integer> vector2) {
        try {
            return chamber.get(vector2.first).get(vector2.second).getLast();
        } catch (NoSuchElementException e) {
            Log.warn("Tentativo di accedere alla cella (" + vector2.first.toString() + ", " + vector2.second.toString() + ") fallito perché è vuota");
        }
        return null;
    }

    public synchronized Entity getEntityOnTop(Entity entity) {
        return getEntityOnTop(new Vector2<>(entity.getRow(), entity.getCol()));
    }

    public synchronized Entity getEntityBelowTheTop(Entity entity) {
        List<Entity> entities = chamber.get(entity.getRow()).get(entity.getCol());
        int index = entities.size() - 2;
        return index >= 0 ? entities.get(index) : null;
    }

    // ----

    public synchronized List<List<List<Entity>>> getChamber() { return Collections.unmodifiableList(chamber); }

    public boolean isInitialized() { return init; }

    public int getNRows() { return nRows; }

    public int getNCols() { return nCols; }

    public Player getPlayer() { return this.player; }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
        ++enemyCounter;
    }

    public void decreaseEnemyCounter() {
        if (--enemyCounter < 0) {
            ++enemyCounter;
        }
    }

    public int getEnemyCounter() {
        return enemyCounter;
    }

    public List<Enemy> getEnemies() { return this.enemies; }

    public void addTraps(Trap trap) { traps.add(trap); }

    public List<Trap> getTraps() { return traps; }

    public synchronized void addProjectile(Projectile projectile) { projectiles.add(projectile); }

    public synchronized List<Projectile> getProjectiles() { return projectiles; }

    public synchronized void addCollectable(Collectable collectable) { collectables.add(collectable); }

    public synchronized List<Collectable> getCollectables() { return collectables; }

    public void addEnvironment(Environment environment) { environments.add(environment); }

    public List<Environment> getEnvironment() { return environments; }

    private void addEntityToDraw(Entity entity, int layer) { drawOrderChamber.add(entity, layer); }

    public List<Layer> getDrawOrderChamber() { return Collections.unmodifiableList(drawOrderChamber.getDrawOrder()); }
}
