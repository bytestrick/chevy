package chevy.view.component;


import chevy.settings.WindowSettings;
import chevy.utils.Load;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageVisualizer extends JComponent {
    private BufferedImage image;
    private float scale = 1f;
    private int width;
    private int height;

    public ImageVisualizer() {}

    public ImageVisualizer(String path) {
        this(path, 1f);
    }

    public ImageVisualizer(String path, float scale) {
        setImage(path, scale);
    }

    public ImageVisualizer(BufferedImage image) {
        setImage(image);
    }

    public ImageVisualizer(BufferedImage image, float scale) {
        setImage(image, scale);
    }

    public void setImage(BufferedImage image, float scale) {
        this.scale = scale;
        this.image = image;
        setOpaque(false);

        width = (int) (scale * image.getWidth() * WindowSettings.scale);
        height = (int) (scale * image.getHeight() * WindowSettings.scale);

        setDimension();
    }

    public void setImage(String path, float scale) {
        setImage(Load.image(path), scale);
    }

    public void setImage(String path) {
        setImage(path, 1f);
    }

    public void setImage(BufferedImage image) {
        setImage(image, 1f);
    }

    private void setDimension() {
        Dimension newDimension = new Dimension(width, height);
        setMaximumSize(newDimension);
        setPreferredSize(newDimension);
        setMinimumSize(newDimension);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(image, 0 , 0, width, height, null);
        }
    }

    public void windowResized(float scale) {
        scale *= this.scale;

        width = (int) (scale * image.getWidth());
        height = (int) (scale * image.getHeight());
        setDimension();
    }
}
