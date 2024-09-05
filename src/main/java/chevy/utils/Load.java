package chevy.utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * Raccolta di metodi per caricare risorse
 */
public final class Load {
    /**
     * @param path il percorso, come "/sprites/img.png"
     * @return l'immagine caricata dalle risorse
     */
    public static BufferedImage image(final String path) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(Load.class.getResource(path)));
        } catch (IOException | NullPointerException e) {
            Log.error("Immagine '" + path + "' non trovata (" + e.getMessage() + ")");
            System.exit(1);
        }
        return image;
    }

    /**
     * @param name   il nome dell'icona senza estensione
     * @param width  larghezza dell'icona desiderata
     * @param height altezza dell'icona desiderata
     * @return l'icona caricata e scalata.
     */
    public static ImageIcon icon(String name, int width, int height) {
        Image image = image("/icons/" + name + ".png");
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * @param name il percorso della risorsa
     * @return il font caricato
     */
    public static Font font(final String name) {
        Font font = null;
        try {
            InputStream is =
                    Objects.requireNonNull(Load.class.getResourceAsStream("/fonts/" + name +
                            ".ttf"));
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException | FontFormatException | NullPointerException e) {
            Log.warn("Font '" + name + "' non caricato: (" + e.getMessage() + ")");
        }
        return font;
    }

    /**
     * @param prefix è il nome della clip senza l'estensione, esempio: {@code coin.wav -> coin}
     * @return la {@link javax.sound.sampled.Clip} caricata, aperta e pronta all'uso
     */
    public static Clip clip(final String prefix) {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        try {
            URL path = Load.class.getResource("/sounds/" + prefix + ".wav");
            if (path == null) {
                throw new NullPointerException();
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(path);
            clip.open(audioIn);
            return clip;
        } catch (NullPointerException | IOException | UnsupportedAudioFileException |
                 SecurityException e) {
            Log.error(prefix + ": " + e.getMessage());
        } catch (LineUnavailableException e) {
            Log.error("Apertura clip fallita: " + e.getMessage());
            System.exit(13);
        }
        return null;
    }
}
