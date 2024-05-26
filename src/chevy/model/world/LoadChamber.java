package chevy.model.world;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoadChamber {
    private static final String CHAMBER_PATH = "src/res/chambers/chamber";
    private static final String LAYER_FILE = "/layer";
    private static final String LAYER_FILE_EXTENSION = ".png";
    private static boolean endChamberLoad = false;


    private static BufferedImage loadImage(int nChamber, int layer) {
        String chamberPath = CHAMBER_PATH + nChamber + LAYER_FILE + layer + LAYER_FILE_EXTENSION;
        BufferedImage chamberImage = null;
        try {
            chamberImage = ImageIO.read(new File(chamberPath));
        }
        catch (IOException ignored) {
            endChamberLoad = true;
            System.out.println(chamberPath);
            System.out.println("il layer" + layer + ".png non si è caricato");
        }

        return chamberImage;
    }


    public static void loadChamber(int nChamber, Chamber chamber) {
        int layer = 0;
        while (!endChamberLoad) {
            BufferedImage chamberImage = loadImage(nChamber, layer);
            if (chamberImage == null)
                return;
            ++layer;

            int nRows = chamberImage.getHeight();
            int nCols = chamberImage.getWidth();
            chamber.initWorld(nRows, nCols);

            for (int i = 0; i < nRows; ++i) {
                for (int j = 0; j < nCols; ++j) {
                    Color color = new Color((chamberImage.getRGB(j, i)));
                    int r = color.getRed();

                    System.out.println(r);

//                    switch (r) {
//
//                    }
                }
            }
        }
    }
}
