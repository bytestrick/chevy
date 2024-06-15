package chevy.view;

import chevy.view.chamber.EntityToImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Image {
    public static BufferedImage load(String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(EntityToImage.class.getResource(path)));
        }
        catch (IOException e) {
            System.out.println("Immagine: " + path + " non trovata");
            System.exit(1);
        }

        return image;
    }
}
