package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Trap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * La classe LoadChamber si occupa del caricamento delle stanze (Chamber) del gioco.
 * Utilizza le immagini per determinare la disposizione delle entità all'interno di ogni stanza.
 */
public class LoadChamber {
    private static final String CHAMBER_PATH = "src/res/chambers/chamber";
    private static final String LAYER_FILE = "/layer";
    private static final String LAYER_FILE_EXTENSION = ".png";

    /**
     * Carica un'immagine di una stanza.
     * @param nChamber Il numero della stanza da caricare.
     * @param layer Strato dell'immagine da caricare.
     * @return L'immagine della stanza, o null se l'immagine non può essere caricata.
     */
    private static BufferedImage loadImage(int nChamber, int layer) {
        String chamberPath = CHAMBER_PATH + nChamber + LAYER_FILE + layer + LAYER_FILE_EXTENSION;
        BufferedImage chamberImage = null;
        try {
            chamberImage = ImageIO.read(new File(chamberPath));
        }
        catch (IOException ignored) {
            System.out.print("[x] " + chamberPath);
            System.out.println(": il layer" + layer + ".png non è stato caricato");
        }
        return chamberImage;
    }

    /**
     * Carica una stanza nel gioco.
     * @param nChamber Il numero della stanza da caricare.
     * @param chamber La stanza in cui caricare le entità.
     * @return true se la stanza è stata caricata correttamente, false altrimenti.
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
                        chamber.addEntityOnTop(entity);
                        switch (entity.getGenericType()) {
                            case Environment.Type.TRAP -> chamber.addTraps((Trap) entity);
                            case DynamicEntity.Type.PROJECTILE -> chamber.addProjectile((Projectile) entity);
                            case LiveEntity.Type.ENEMY -> chamber.addEnemy((Enemy) entity);
                            case LiveEntity.Type.PLAYER -> chamber.setPlayer((Player) entity);
                            case Entity.Type.COLLECTABLE -> chamber.addCollectable((Collectable) entity);
                            default -> {}
                        }
//                        if (entity instanceof Player player)
//                            chamber.setPlayer(player);
//                        else if (entity instanceof Enemy enemy) {
//                            chamber.addEnemy(enemy);
//                        }
//                        else if (entity instanceof Trap trap) {
//                            chamber.addTraps(trap);
//                        }
                    }
                }
            }
            chamberImage = loadImage(nChamber, ++layer);
        }
        return layer != 0;
    }
}
