package chevy.view.entityView;

import chevy.model.entity.staticEntity.environment.Ground;
import chevy.model.entity.staticEntity.environment.GroundTypes;
import chevy.model.entity.staticEntity.environment.Wall;
import chevy.settings.GameSettings;
import chevy.utilz.Vector2;
import chevy.view.Image;

import java.awt.image.BufferedImage;

public class GroundView extends EntityView {
    private static BufferedImage GROUND_CENTRAL = null;
    private static BufferedImage GROUND_LEFT = null;
    private static BufferedImage GROUND_RIGHT = null;
    private static BufferedImage GROUND_TOP = null;
    private static BufferedImage GROUND_INTERIOR_CORNER_TOP_LEFT = null;
    private static BufferedImage GROUND_INTERIOR_CORNER_TOP_RIGHT = null;
    private static final String COMMON_PATH = "/assets/chamberTiles/groundTiles/";

    private final Ground ground;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);

    public GroundView(Ground ground) {
        this.ground = ground;
    }


    @Override
    public BufferedImage getCurrentFrame() {
        return switch (ground.getSpecificType()) {
            case GroundTypes.CENTRAL -> {
                if (GROUND_CENTRAL == null)
                    GROUND_CENTRAL = Image.load(COMMON_PATH + "central.png");
                yield GROUND_CENTRAL;
            }
            case GroundTypes.LEFT -> {
                if (GROUND_LEFT == null)
                    GROUND_LEFT = Image.load(COMMON_PATH + "left.png");
                yield GROUND_LEFT;
            }
            case GroundTypes.RIGHT -> {
                if (GROUND_RIGHT == null)
                    GROUND_RIGHT = Image.load(COMMON_PATH + "right.png");
                yield GROUND_RIGHT;
            }
            case GroundTypes.TOP -> {
                if (GROUND_TOP == null)
                    GROUND_TOP = Image.load(COMMON_PATH + "top.png");
                yield GROUND_TOP;
            }
            case GroundTypes.INTERIOR_CORNER_TOP_LEFT -> {
                if (GROUND_INTERIOR_CORNER_TOP_LEFT == null)
                    GROUND_INTERIOR_CORNER_TOP_LEFT = Image.load(COMMON_PATH + "interiorCornerTopLeft.png");
                yield GROUND_INTERIOR_CORNER_TOP_LEFT;
            }
            case GroundTypes.INTERIOR_CORNER_TOP_RIGHT -> {
                if (GROUND_INTERIOR_CORNER_TOP_RIGHT == null)
                    GROUND_INTERIOR_CORNER_TOP_RIGHT = Image.load(COMMON_PATH + "interiorCornerTopRight.png");
                yield GROUND_INTERIOR_CORNER_TOP_RIGHT;
            }
            default -> null;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        position.changeFirst((double) ground.getCol());
        position.changeSecond((double) ground.getRow());
        return position;
    }
}
