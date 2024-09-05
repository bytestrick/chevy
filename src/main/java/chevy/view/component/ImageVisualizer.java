package chevy.view.component;


import chevy.utils.Load;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class ImageVisualizer extends JComponent {
    private BufferedImage image;
    private float scale = 1f;
    private int width;
    private int height;

    public ImageVisualizer() {}

    public ImageVisualizer(String path) {
        this(path, 1f);
    }

    public ImageVisualizer(String path, float scale) {
        setOpaque(false);
        setImage(path, scale);
    }

    public ImageVisualizer(BufferedImage image) {
        setImage(image);
    }

    public ImageVisualizer(BufferedImage image, float scale) {
        setOpaque(false);
        setImage(image, scale);
    }

    public void setImage(BufferedImage image, float scale) {
        this.scale = scale;
        this.image = image;

        width = (int) (scale * image.getWidth());
        height = (int) (scale * image.getHeight());

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

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
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
        width = (int) Math.ceil(scale * image.getWidth());
        height = (int) Math.ceil(scale * image.getHeight());
        setDimension();
    }
}
