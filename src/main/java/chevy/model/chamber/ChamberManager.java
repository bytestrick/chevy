package chevy.model.chamber;

import chevy.control.ChamberController;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.utils.Log;
import chevy.view.Menu;
import chevy.view.chamber.EntityToEntityView;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Gestisce l'insieme di stanze (Chamber) nel gioco.
 * Utilizza il pattern Singleton per garantire che esista una sola istanza di ChamberManager.
 */
public class ChamberManager {
    public static final int NUMBER_OF_CHAMBERS = 7;
    private static final Chamber[] chambers = new Chamber[NUMBER_OF_CHAMBERS];
    /** Indice della stanza corrente nel gioco. */
    private static int currentChamberIndex = 0;

    /**
     * Carica un layer che comporrà la stanza.
     *
     * @param chamber l'indice della stanza da caricare
     * @param layer   strato dell'immagine da caricare
     * @return l'immagine della stanza, o null se l'immagine non può essere caricata
     */
    private static BufferedImage loadLayer(int chamber, int layer) {
        final String path = "/chambers/chamber" + chamber + "/layer" + layer + ".png";
        try {
            return ImageIO.read(Objects.requireNonNull(ChamberManager.class.getResource(path)));
        } catch (NullPointerException ignored) {
            Log.info("La stanza " + chamber + " ha " + layer + " strati");
        } catch (IOException e) {
            Log.warn("Caricamento della stanza " + chamber + " fallito: " + e.getMessage());
            System.exit(96);
        }
        return null;
    }

    /**
     * Carica una stanza
     *
     * @param index il numero della stanza da caricare (livello). Parte da 0.
     * @return Chamber caricata
     */
    private static Chamber loadChamber(int index) {
        Log.info("Caricamento della stanza " + index);
        Chamber chamber = new Chamber();
        int layer = 0;
        BufferedImage chamberImage = loadLayer(index, layer);

        while (chamberImage != null) {
            final Dimension size = new Dimension(chamberImage.getWidth(), chamberImage.getHeight());
            if (!chamber.isInitialized()) {
                chamber.initWorld(size);
            }

            for (int i = 0; i < size.height; ++i) {
                for (int j = 0; j < size.width; ++j) {
                    Color color = new Color(chamberImage.getRGB(j, i));
                    int r = color.getRed();
                    if (r != 0) {
                        Entity entity = EntityFromColor.get(r, i, j);
                        if (entity != null) {
                            switch (entity.getGenericType()) {
                                case Environment.Type.TRAP -> chamber.addTraps((Trap) entity);
                                case DynamicEntity.Type.PROJECTILE ->
                                        chamber.addProjectile((Projectile) entity);
                                case LiveEntity.Type.ENEMY -> chamber.addEnemy((Enemy) entity);
                                case LiveEntity.Type.PLAYER -> {
                                    entity.setToDraw(false);
                                    chamber.setPlayer((Player) entity);
                                }
                                case Entity.Type.COLLECTABLE, Collectable.Type.POWER_UP ->
                                        chamber.addCollectable((Collectable) entity);
                                case Entity.Type.ENVIRONMENT ->
                                        chamber.addEnvironment((Environment) entity);
                                default -> {}
                            }
                            chamber.addEntityOnTop(entity);
                        }
                    }
                }
            }
            chamberImage = loadLayer(index, ++layer);
        }
        if (layer > 0) {
            return chamber;
        }
        throw new RuntimeException("Tentativo di caricare la stanza " + index + " fallito");
    }

    /**
     * Sblocca e passa alla stanza successiva
     */
    public static void nextChamber() {
        Menu.incrementLevel();
        enterChamber(currentChamberIndex + 1);
    }

    public static boolean isLastChamber() {
        return currentChamberIndex == NUMBER_OF_CHAMBERS - 1;
    }

    /**
     * @return la stanza corrente
     */
    public static Chamber getCurrentChamber() {return chambers[currentChamberIndex];}

    /**
     * Imposta la stanza corrente a index. Se la stanza è già caricata la invalida e ne forza il
     * caricamento.
     * Predispone il gioco.
     *
     * @param index della stanza
     */
    public static void enterChamber(final int index) {
        if (index < NUMBER_OF_CHAMBERS) {
            currentChamberIndex = index;
            chambers[currentChamberIndex] = loadChamber(index);
            ChamberController.refresh();
            // Invalida la view del player corrente
            EntityToEntityView.entityView.remove(getCurrentChamber().getPlayer());
            GameLoop.start();
            Sound.startMusic(); // 🎵
        }
    }

    public static int getCurrentChamberIndex() {return currentChamberIndex;}
}
