package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Trap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadChamber {
    private static final String CHAMBER_PATH = "src/res/chambers/chamber";
    private static final String LAYER_FILE = "/layer";
    private static final String LAYER_FILE_EXTENSION = ".png";


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
                        if (entity instanceof Player player)
                            chamber.setPlayer(player);
                        else if (entity instanceof Enemy enemy) {
                            chamber.addEnemy(enemy);
                        }
                        else if (entity instanceof Trap trap) {
                            if (trap instanceof SpikedFloor || trap instanceof Totem)
                                chamber.addTraps(trap);
                        }
                    }
                }
            }
            chamberImage = loadImage(nChamber, ++layer);
        }
        return layer != 0;
    }
}
