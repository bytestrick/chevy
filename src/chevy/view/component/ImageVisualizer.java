package chevy.view.component;


import chevy.utils.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageVisualizer extends JComponent {
    private BufferedImage image;
    private int width;
    private int height;
    private float scale = 1f;

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
        setOpaque(false);
        this.image = image;
        width = (int) (image.getWidth() / this.scale * scale);
        height = (int) (image.getHeight() / this.scale * scale);
        this.scale = scale;
        setDimension();
    }

    public void setImage(String path, float scale) {
        setImage(Image.load(path), scale);
    }

    public void setImage(String path) {
        setImage(path, 1f);
    }

    public void setImage(BufferedImage image) {
        setImage(image, 1f);
    }

    private void setDimension() {
        Dimension dimension = new Dimension(width, height);
        setMaximumSize(dimension);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null)
            g.drawImage(image, 0 , 0, width, height, null);
    }
}
