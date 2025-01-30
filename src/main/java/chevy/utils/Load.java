package chevy.utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Collection of methods to load resources
 */
public final class Load {
    private static final int ICON_SIZE = 32;

    /**
     * @param path the path, such as: "/sprites/img.png"
     * @return the loaded image
     */
    public static BufferedImage image(String path) {
        BufferedImage image;
        try {
            final URL input = Load.class.getResource(path);
            assert input != null : "Resource not found: " + path;
            image = ImageIO.read(input);
        } catch (IOException e) {
            throw new RuntimeException("Image not found: " + path + ": " + e.getMessage() + ")");
        }
        return image;
    }

    /**
     * @param name   name of the icon without extension
     * @param width  desired width of the icon
     * @param height desired height of the icon
     * @return the loaded icon with the specified dimensions
     */
    public static Icon icon(String name, int width, int height) {
        Image image = image("/icons/" + name + ".png");
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    /**
     * @param name name of the icon without extension
     * @return the loaded icon with size 32x32 px
     */
    public static Icon icon(String name) {
        return Load.icon(name, ICON_SIZE, ICON_SIZE);
    }

    /**
     * @param name path of the gif without extension
     * @return la gif
     */
    public static Icon gif(String name) {
        URL url = Load.class.getResource("/" + name + ".gif");
        assert url != null;
        return new ImageIcon(url);
    }

    /**
     * @param name resource name
     * @return the loaded font
     */
    public static Font font(String name) {
        Font font = null;
        try {
            try (InputStream is = Load.class.getResourceAsStream("/fonts/" + name + ".ttf")) {
                assert is != null : "font not found";
                font = Font.createFont(Font.TRUETYPE_FONT, is);
            }
        } catch (IOException | FontFormatException e) {
            Log.warn("Font not loaded: " + name + ": " + e.getMessage());
        }
        return font;
    }

    /**
     * @param prefix name of the clip without extension
     * @return the loaded {@link javax.sound.sampled.Clip}, opened and ready to play
     */
    public static Clip clip(String prefix) {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        try {
            URL url = Load.class.getResource("/sounds/" + prefix + ".wav");
            assert url != null : "Clip not found: " + prefix;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip.open(audioIn);
            return clip;
        } catch (IOException | UnsupportedAudioFileException | SecurityException e) {
            throw new RuntimeException(prefix + ": " + e.getMessage());
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Failed to open clip: " + e.getMessage());
        }
    }
}
