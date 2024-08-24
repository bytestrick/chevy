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
 * Carica le Chamber del gioco.
 * Utilizza le immagini per determinare la disposizione delle entità all'interno di ogni stanza.
 */
public class ChamberLoader {
    private static final String CHAMBER_PATH = "src/res/chambers/chamber";
    private static final String LAYER_FILE = "/layer";
    private static final String LAYER_FILE_EXTENSION = ".png";

    /**
     * Carica un layer che comporrà la stanza.
     *
     * @param nChamber il numero della stanza da caricare
     * @param layer strato dell'immagine da caricare
     * @return l'immagine della stanza, o null se l'immagine non può essere caricata
     */
    private static BufferedImage loadLayer(int nChamber, int layer) {
        String chamberPath = CHAMBER_PATH + nChamber + LAYER_FILE + layer + LAYER_FILE_EXTENSION;
        BufferedImage chamberImage = null;
        try {
            chamberImage = ImageIO.read(new File(chamberPath));
        } catch (IOException ignored) {
            Log.warn(chamberPath + ": il layer" + ".png non è stato caricato");
        }
        return chamberImage;
    }

    /**
     * Carica una stanza nel gioco
     *
     * @param nChamber Il numero della stanza da caricare (livello)
     * @return true se la stanza è stata caricata correttamente, false altrimenti
     */
    public static boolean loadChamber(final int nChamber, Chamber chamber) {
        int layer = 0;
        BufferedImage chamberImage = loadLayer(nChamber, layer);

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
            chamberImage = loadLayer(nChamber, ++layer);
        }
        return layer != 0;
    }
}
