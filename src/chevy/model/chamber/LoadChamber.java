package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
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
public class LoadChamber {
    private static final String CHAMBER_PATH = "src/res/chambers/chamber";
    private static final String LAYER_FILE = "/layer";
    private static final String LAYER_FILE_EXTENSION = ".png";

    /**
     * Carica un'immagine di una stanza.
     *
     * @param nChamber il numero della stanza da caricare
     * @param layer    strato dell'immagine da caricare
     * @return l'immagine della stanza, o null se l'immagine non può essere caricata
     */
    private static BufferedImage loadImage(int nChamber, int layer) {
        String chamberPath = CHAMBER_PATH + nChamber + LAYER_FILE + layer + LAYER_FILE_EXTENSION;
        BufferedImage chamberImage = null;
        try {
            chamberImage = ImageIO.read(new File(chamberPath));
        } catch (IOException ignored) {
            Log.warn(chamberPath + ": il layer" + layer + ".png non è stato caricato");
        }
        return chamberImage;
    }

    /**
     * Carica una stanza nel gioco.
     *
     * @param nChamber Il numero della stanza da caricare.
     * @param chamber  La stanza in cui caricare le entità.
     * @return true se la stanza è stata caricata correttamente, false altrimenti
     */
    public static boolean loadChamber(int nChamber, Chamber chamber) {
        int layer = 0;
        BufferedImage chamberImage = loadImage(nChamber, layer);

        while (chamberImage != null) {
            int nRows = chamberImage.getHeight();
            int nCols = chamberImage.getWidth();
            if (!chamber.isInitialized()) {
                chamber.initWorld(nRows, nCols);
            }

            for (int i = 0; i < nRows; ++i) {
                for (int j = 0; j < nCols; ++j) {
                    Color color = new Color((chamberImage.getRGB(j, i)));
                    int r = color.getRed();
                    if (r != 0) {
                        Entity entity = EntityFromColor.get(r, i, j);
                        assert entity != null;
                        chamber.addEntityOnTop(entity);
                        switch (entity) {
                            case Player player -> chamber.setPlayer(player);
                            case Enemy enemy -> chamber.addEnemy(enemy);
                            case Trap trap -> chamber.addTraps(trap);
                            default -> {
                            }
                        }
                    }
                }
            }
            chamberImage = loadImage(nChamber, ++layer);
        }
        return layer != 0;
    }
}