package chevy.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Operazioni su immagini.
 */
public class Image {

    /**
     * Carica un'immagine dalle risorse.
     *
     * @param path il percorso, tipo "/res/img.jpg"
     */
    public static BufferedImage load(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(Image.class.getResource(path)));
        } catch (IOException | NullPointerException e) {
            Log.error("Immagine '" + path + "' non trovata.");
            System.exit(1);
        }
        return image;
    }
}
