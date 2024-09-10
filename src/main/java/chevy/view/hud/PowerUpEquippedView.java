package chevy.view.hud;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.view.Window;
import chevy.view.component.ImageVisualizer;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.FlowLayout;

public final class PowerUpEquippedView extends JComponent {
    private static final String RES = "/sprites/powerUpIcons/";
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
        String path = RES;

        switch ((PowerUp.Type) powerUp.getType()) {
            case AGILITY -> path += "agility";
            case ANGEL_RING -> path += "angelRing";
            case COLD_HEART -> path += "coldHeart";
            case COIN_OF_GREED -> path += "coinOfGreed";
            case BROKEN_ARROWS -> path += "brokenSArrows";
            case GOLD_ARROW -> path += "goldenArrows";
            case HEALING_FLOOD -> path += "healingFlood";
            case HEDGEHOG_SPINES -> path += "hedgehogSpines";
            case HOBNAIL_BOOTS -> path += "hobnailBoots";
            case HOLY_SHIELD -> path += "holyShield";
            case HOT_HEART -> path += "hotHeart";
            case KEY_S_KEEPER -> path += "keySKeeper";
            case LONG_SWORD -> path += "longSword";
            case SLIME_PIECE -> path += "slimePiece";
            case STONE_BOOTS -> path += "stoneBoots";
            case VAMPIRE_FANGS -> path += "vampireFangs";
        }

        ImageVisualizer frame = new ImageVisualizer(path + ".png", scale * Window.scale);
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