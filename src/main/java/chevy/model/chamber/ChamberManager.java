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
import chevy.service.Data;
import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.utils.Log;
import chevy.view.Window;
import chevy.view.chamber.EntityToEntityView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Manages the set of {@link Chamber} in the game
 */
public final class ChamberManager {
    public static final int NUMBER_OF_CHAMBERS = 10;
    private static final Chamber[] chambers = new Chamber[NUMBER_OF_CHAMBERS];
    private static Window window;

    /**
     * Index of the current room in the game.
     */
    private static int currentChamberIndex;

    /**
     * Loads a layer that will compose the room.
     *
     * @param chamber index of the room to load
     * @param layer   layer of the room to load
     * @return the image of the room, or null if the image cannot be loaded
     */
    private static BufferedImage loadLayer(int chamber, int layer) {
        final String path = "/chambers/chamber" + chamber + "/layer" + layer + ".png";
        try {
            final URL input = ChamberManager.class.getResource(path);
            if (input == null) {
                return null;
            }
            return ImageIO.read(input);
        } catch (NullPointerException ignored) {
            Log.info("Room " + chamber + " has " + layer + " layers");
        } catch (IOException e) {
            Log.warn("Loading of room " + chamber + " failed: " + e.getMessage());
            System.exit(96);
        }
        return null;
    }

    /**
     * Load a room
     *
     * @param index the number of the room to load, starting from 0
     * @return the room loaded
     */
    private static Chamber loadChamber(int index) {
        Log.info("Loading room " + index);
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
                                case DynamicEntity.Type.PROJECTILE -> chamber.addProjectile((Projectile) entity);
                                case LiveEntity.Type.ENEMY -> chamber.addEnemy((Enemy) entity);
                                case LiveEntity.Type.PLAYER -> {
                                    entity.setShouldDraw(false);
                                    chamber.setPlayer((Player) entity);
                                }
                                case Entity.Type.COLLECTABLE, Collectable.Type.POWER_UP ->
                                        chamber.addCollectable((Collectable) entity);
                                case Entity.Type.ENVIRONMENT -> chamber.addEnvironment((Environment) entity);
                                default -> {
                                }
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
        throw new RuntimeException("Attempt to load room " + index + " failed");
    }

    /**
     * Unlock and go to the next room
     */
    public static void nextChamber() {
        enterChamber(currentChamberIndex + 1);
    }

    public static boolean isLastChamber() {
        return currentChamberIndex == NUMBER_OF_CHAMBERS - 1;
    }

    public static Chamber getCurrentChamber() {
        return chambers[currentChamberIndex];
    }

    /**
     * Check if the current room is already loaded, if not, load it.
     *
     * @param index della stanza
     */
    public static void enterChamber(final int index) {
        if (index < NUMBER_OF_CHAMBERS) {
            currentChamberIndex = index;
            Data.set("menu.level", currentChamberIndex);
            chambers[currentChamberIndex] = loadChamber(index);
            window.getGamePanel().setWindowTitle();
            ChamberController.refresh();
            // invalidate the view of the current player to force a reset
            EntityToEntityView.entityView.remove(getCurrentChamber().getPlayer());
            GameLoop.start();
            Sound.startMusic(Sound.Music.NEW_SONG); // 🎵
        }
    }

    public static int getCurrentChamberIndex() {
        return currentChamberIndex;
    }

    public static void setWindow(Window window) {
        ChamberManager.window = window;
    }
}
