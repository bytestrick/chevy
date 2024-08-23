package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.Wall;
import chevy.utils.Image;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;

import java.awt.image.BufferedImage;

public class WallView extends EntityView {
    private static final String COMMON_PATH = "/assets/chamberTiles/wallTiles/";
    private static BufferedImage TOP_IMAGE = null;
    private static BufferedImage BOTTOM_IMAGE = null;
    private static BufferedImage LEFT_IMAGE = null;
    private static BufferedImage RIGHT_IMAGE = null;
    private static BufferedImage EXTERNAL_CORNER_TOP_LEFT_IMAGE = null;
    private static BufferedImage EXTERNAL_CORNER_TOP_RIGHT_IMAGE = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_LEFT_IMAGE = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_RIGHT_IMAGE = null;

    private final Wall wall;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);

    public WallView(Wall wall) {
        this.wall = wall;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return switch (wall.getSpecificType()) {
            case TOP -> {
                if (TOP_IMAGE == null) {
                    TOP_IMAGE = Image.load(COMMON_PATH + "top.png");
                }
                yield TOP_IMAGE;
            }
            case BOTTOM -> {
                if (BOTTOM_IMAGE == null) {
                    BOTTOM_IMAGE = Image.load(COMMON_PATH + "bottom.png");
                }
                yield BOTTOM_IMAGE;
            }
            case LEFT -> {
                if (LEFT_IMAGE == null) {
                    LEFT_IMAGE = Image.load(COMMON_PATH + "left.png");
                }
                yield LEFT_IMAGE;
            }
            case RIGHT -> {
                if (RIGHT_IMAGE == null) {
                    RIGHT_IMAGE = Image.load(COMMON_PATH + "right.png");
                }
                yield RIGHT_IMAGE;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT -> {
                if (EXTERNAL_CORNER_BOTTOM_LEFT_IMAGE == null) {
                    EXTERNAL_CORNER_BOTTOM_LEFT_IMAGE = Image.load(COMMON_PATH + "externalCornerBottomLeft.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_LEFT_IMAGE;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT -> {
                if (EXTERNAL_CORNER_BOTTOM_RIGHT_IMAGE == null) {
                    EXTERNAL_CORNER_BOTTOM_RIGHT_IMAGE = Image.load(COMMON_PATH + "externalCornerBottomRight.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_RIGHT_IMAGE;
            }
            case EXTERNAL_CORNER_TOP_LEFT -> {
                if (EXTERNAL_CORNER_TOP_LEFT_IMAGE == null) {
                    EXTERNAL_CORNER_TOP_LEFT_IMAGE = Image.load(COMMON_PATH + "externalCornerTopLeft.png");
                }
                yield EXTERNAL_CORNER_TOP_LEFT_IMAGE;
            }
            case EXTERNAL_CORNER_TOP_RIGHT -> {
                if (EXTERNAL_CORNER_TOP_RIGHT_IMAGE == null) {
                    EXTERNAL_CORNER_TOP_RIGHT_IMAGE = Image.load(COMMON_PATH + "externalCornerTopRight.png");
                }
                yield EXTERNAL_CORNER_TOP_RIGHT_IMAGE;
            }
            default -> null;
        };
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        position.changeFirst((double) wall.getCol());
        position.changeSecond((double) wall.getRow());
        return position;
    }
}