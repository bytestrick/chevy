package chevy.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Image {
    public static BufferedImage load(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(Image.class.getResource(path)));
        }
        catch (IOException | NullPointerException e) {
            System.out.println("Imagine: " + path + " non trovata");
            System.exit(1);
        }
        return image;
    }
}
