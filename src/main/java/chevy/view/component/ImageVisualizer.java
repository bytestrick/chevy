package chevy.view.component;

import chevy.utils.Load;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public final class ImageVisualizer extends JComponent {
    private BufferedImage image;
    private int width;
    private int height;

    public ImageVisualizer(String path, float scale) {
        setOpaque(false);
        setImage(path, scale);
    }

    ImageVisualizer(BufferedImage image, float scale) {
        setOpaque(false);
        setImage(image, scale);
    }

    private void setDimension() {
        Dimension newDimension = new Dimension(width, height);
        setMaximumSize(newDimension);
        setPreferredSize(newDimension);
        setMinimumSize(newDimension);
        revalidate();
        repaint();
    }

    public void windowResized(float scale) {
        width = (int) Math.ceil(scale * image.getWidth());
        height = (int) Math.ceil(scale * image.getHeight());
        setDimension();
    }

    private void setImage(BufferedImage image, float scale) {
        this.image = image;

        width = Math.round(scale * image.getWidth());
        height = Math.round(scale * image.getHeight());

        setDimension();
    }

    private void setImage(String path, float scale) {setImage(Load.image(path), scale);}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(image, 0, 0, width, height, null);
        }
    }

    @Override
    public int getWidth() {return width;}

    @Override
    public int getHeight() {return height;}
}
