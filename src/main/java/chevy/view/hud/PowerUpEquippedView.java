package chevy.view.hud;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.view.Window;
import chevy.view.component.ImageVisualizer;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.FlowLayout;

public final class PowerUpEquippedView extends JComponent {
    private static final int spacing = 2;
    private final float scale;
    private int frameWidth;

    PowerUpEquippedView(float scale) {
        this.scale = scale;
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, spacing, spacing));
        setDimension();
    }

    public void add(PowerUp powerUp) {
        ImageVisualizer frame =
                new ImageVisualizer("/sprites/powerUpIcons/" + powerUp.getType().toString().toLowerCase() + ".png", scale * Window.scale);
        frame.setToolTipText(powerUp.getDescription());
        add(frame);
        setDimension();
    }

    private void setDimension() {
        calculateFrameWidth();
        Dimension dimension = new Dimension(2 * frameWidth, Window.size.height);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        revalidate();
    }

    private void calculateFrameWidth() {
        if (getComponentCount() > 0) {
            frameWidth = getComponent(0).getWidth() + 1 + spacing;
        } else {
            frameWidth = 0;
        }
    }

    public void clear() {
        int n = getComponentCount();
        for (; n > 0; --n) {
            remove(n - 1);
        }
    }

    public void windowResized(float scale) {
        scale *= this.scale;
        int n = getComponentCount();
        for (int i = 0; i < n; ++i) {
            ImageVisualizer frame = (ImageVisualizer) getComponent(i);
            frame.windowResized(scale);
        }
        setDimension();
    }
}