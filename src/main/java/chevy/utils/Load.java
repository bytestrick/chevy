package chevy.utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Raccolta di metodi per caricare risorse
 */
public final class Load {
    private static final int ICON_SIZE = 32;

    /**
     * @param path il percorso, come "/sprites/img.png"
     * @return l'immagine caricata dalle risorse
     */
    public static BufferedImage image(final String path) {
        BufferedImage image = null;
        try {
            final URL input = Load.class.getResource(path);
            assert input != null : "risorsa non trovata";
            image = ImageIO.read(input);
        } catch (IOException e) {
            Log.error("Immagine '" + path + "' non trovata (" + e.getMessage() + ")");
            System.exit(1);
        }
        return image;
    }

    /**
     * @param name   il nome dell'icona senza estensione
     * @param width  larghezza dell'icona desiderata
     * @param height altezza dell'icona desiderata
     * @return l'icona caricata e scalata
     */
    public static Icon icon(String name, int width, int height) {
        Image image = image("/icons/" + name + ".png");
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * @param name il nome dell'icona senza estensione
     * @return l'icona caricata con dimensione 32x32
     */
    public static Icon icon(String name) {
        Image image = image("/icons/" + name + ".png");
        return new ImageIcon(image.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH));
    }

    /**
     * @param name il percorso della risorsa
     * @return il font caricato
     */
    public static Font font(final String name) {
        Font font = null;
        try {
            try (InputStream is = Load.class.getResourceAsStream("/fonts/" + name + ".ttf")) {
                assert is != null : "risorsa non trovata";
                font = Font.createFont(Font.TRUETYPE_FONT, is);
            }
        } catch (IOException | FontFormatException e) {
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
            URL url = Load.class.getResource("/sounds/" + prefix + ".wav");
            assert url != null : "risorsa non trovata";
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip.open(audioIn);
            return clip;
        } catch (IOException | UnsupportedAudioFileException |
                 SecurityException e) {
            Log.error(prefix + ": " + e.getMessage());
        } catch (LineUnavailableException e) {
            Log.error("Apertura clip fallita: " + e.getMessage());
            System.exit(13);
        }
        return null;
    }
}
