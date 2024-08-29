package chevy.view.hud;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.settings.WindowSettings;
import chevy.view.component.ImageVisualizer;

import javax.swing.*;
import java.awt.*;

public class PowerUpEquippedView extends JComponent {
    private static final String POWER_UP_RESOURCES = "/sprites/powerUpIcons/";
    private float scale;
    private final int nFrame = 3;
    private int frameWidth = 0;
    private int spacing = 2;

    public PowerUpEquippedView(float scale) {
        this.scale = scale;

        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, spacing, spacing));

        setDimension();
    }

    public void add(PowerUp powerUp) {
        String path = POWER_UP_RESOURCES;

        switch ((PowerUp.Type) powerUp.getSpecificType()) {
            case AGILITY -> path += "agility";
            case ANGEL_RING -> path += "angelRing";
            case COLD_HEART -> path += "coldHeart";
            case COIN_OF_GREED -> path += "coinOfGreed";
            case BROKEN_ARROWS -> path += "brokenArrow";
            case GOLD_ARROW -> path += "goldenArrow";
            case HEALING_FLOOD -> path += "healingFlood";
            case HEDGEHOG_SPINES -> path += "hedgehogSpied";
            case HOBNAIL_BOOTS -> path += "hobnailBoots";
            case HOLY_SHIELD -> path += "holyShield";
            case HOT_HEART -> path += "hotHeart";
            case KEY_S_KEEPER -> path += "keySKeeper";
            case LONG_SWORD -> path += "longSword";
            case SLIME_PIECE -> path += "slimePiece";
            case STONE_BOOTS -> path += "stoneBoots";
            case PIECE_OF_BONE -> path += "pieceOfBone";
            case VAMPIRE_FANGS -> path += "vampireFangs";
        }

        add(new ImageVisualizer(path + ".png", scale * WindowSettings.scale));
        setDimension();
    }

    private void setDimension() {
        calculateFrameWidth();
        Dimension dimension = new Dimension(nFrame * frameWidth, WindowSettings.WINDOW_HEIGHT);
        System.out.println(dimension);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        revalidate();
    }

    private void calculateFrameWidth() {
        if (getComponentCount() > 0) {
            frameWidth = getComponent(0).getWidth() + 1 + spacing;
        }
        else
            frameWidth = 0;
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
