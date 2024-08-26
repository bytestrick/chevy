package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.utils.Log;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Gestisce l'insieme di stanze (Chamber) nel gioco.
 * Utilizza il pattern Singleton per garantire che esista una sola istanza di ChamberManager.
 */
public class ChamberManager {
    public static final int NUMBER_OF_CHAMBERS = 6;
    private static final Chamber[] chambers = new Chamber[NUMBER_OF_CHAMBERS];
    private static final String CHAMBER_PATH = "src/res/chambers/chamber";
    private static ChamberManager instance = null;
    private static int currentChamberIndex = 0; // Indice della stanza corrente nel gioco.

    /**
     * Restituisce l'istanza Singleton di ChamberManager. Se non esiste, la crea.
     *
     * @return L'istanza Singleton di ChamberManager
     */
    public static ChamberManager getInstance() {
        if (instance == null) {
            instance = new ChamberManager();
        }
        return instance;
    }

    /**
     * Carica un layer che comporrà la stanza.
     *
     * @param chamber l'indice della stanza da caricare
     * @param layer   strato dell'immagine da caricare
     * @return l'immagine della stanza, o null se l'immagine non può essere caricata
     */
    private static BufferedImage loadLayer(int chamber, int layer) {
        String chamberPath = CHAMBER_PATH + chamber + "/layer" + layer + ".png";
        BufferedImage chamberImage = null;
        try {
            chamberImage = ImageIO.read(new File(chamberPath));
        } catch (IOException ignored) {
            Log.warn(chamberPath + ": il layer" + ".png non è stato caricato");
        }
        return chamberImage;
    }

    /**
     * Carica una stanza
     *
     * @param index il numero della stanza da caricare (livello). Parte da 0.
     * @return la Chamber. Avrà valore null se il caricamento è fallito.
     */
    private static Chamber loadChamber(int index) {
        Chamber chamber = new Chamber();
        int layer = 0;
        BufferedImage chamberImage = loadLayer(index, layer);

        while (chamberImage != null) {
            int nRows = chamberImage.getHeight();
            int nCols = chamberImage.getWidth();
            if (!chamber.isInitialized()) {
                chamber.initWorld(nRows, nCols);
            }

            for (int i = 0; i < nRows; ++i) {
                for (int j = 0; j < nCols; ++j) {
                    Color color = new Color(chamberImage.getRGB(j, i));
                    int r = color.getRed();
                    if (r != 0) {
                        Entity entity = EntityFromColor.get(r, i, j);
                        assert entity != null;
                        switch (entity.getGenericType()) {
                            case Environment.Type.TRAP -> chamber.addTraps((Trap) entity);
                            case DynamicEntity.Type.PROJECTILE -> chamber.addProjectile((Projectile) entity);
                            case LiveEntity.Type.ENEMY -> chamber.addEnemy((Enemy) entity);
                            case LiveEntity.Type.PLAYER -> {
                                entity.setToDraw(false);
                                chamber.setPlayer((Player) entity);
                            }
                            case Entity.Type.COLLECTABLE, Collectable.Type.POWER_UP ->
                                    chamber.addCollectable((Collectable) entity);
                            case Entity.Type.ENVIRONMENT -> chamber.addEnvironment((Environment) entity);
                            default -> { }
                        }
                        chamber.addEntityOnTop(entity);
                    }
                }
            }
            chamberImage = loadLayer(index, ++layer);
        }
        if (layer > 0) {
            return chamber;
        }
        return null;
    }

    /**
     * @param index indice della stanza
     * @return la i-esima stanza. Se non esiste la carica.
     */
    private Chamber getChamber(int index) {
        if (chambers[index] == null) {
            Log.info("Carico la chamber " + index);
            Chamber chamber = loadChamber(index);
            if (chamber == null) {
                Log.error("Tentativo di caricare la chamber " + index + " fallito");
                System.exit(1);
            }
            chambers[index] = chamber;
        }
        return chambers[index];
    }

    /**
     * Passa alla stanza successiva
     */
    public void advanceChamber() { ++currentChamberIndex; }

    /**
     * @return la stanza corrente
     */
    public Chamber getCurrentChamber() { return getChamber(currentChamberIndex); }

    public int getCurrentChamberIndex() { return currentChamberIndex; }

    /**
     * Imposta la stanza corrente a index. Se la stanza è già caricata la invalida e ne forza il caricamento.
     *
     * @param index della stanza
     */
    public void requireChamber(int index) {
        currentChamberIndex = index;
        chambers[index] = null; // al prossimo getCurrentChamber(), getChamber() eseguirà il caricamento
    }
}