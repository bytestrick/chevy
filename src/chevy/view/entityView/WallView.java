package chevy.view.entityView;

import chevy.model.entity.staticEntity.environment.Wall;
import chevy.model.entity.staticEntity.environment.WallTypes;
import chevy.settings.GameSettings;
import chevy.utilz.Vector2;
import chevy.view.Image;

import java.awt.image.BufferedImage;

public class WallView extends EntityView {
    private static final String COMMON_PATH = "/assets/chamberTiles/wallTiles/";
    private static BufferedImage WALL_TOP = null;
    private static BufferedImage WALL_BOTTOM = null;
    private static BufferedImage WALL_LEFT = null;
    private static BufferedImage WALL_RIGHT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_TOP_LEFT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_TOP_RIGHT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_BOTTOM_LEFT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_BOTTOM_RIGHT = null;

    private final Wall wall;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);


    public WallView(Wall wall) {
        this.wall = wall;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return switch (wall.getSpecificType()) {
            case WallTypes.TOP -> {
                if (WALL_TOP == null)
                    WALL_TOP = Image.load(COMMON_PATH + "top.png");
                yield WALL_TOP;
            }
            case WallTypes.BOTTOM -> {
                if (WALL_BOTTOM == null)
                    WALL_BOTTOM = Image.load(COMMON_PATH + "bottom.png");
                yield WALL_BOTTOM;
            }
            case WallTypes.LEFT -> {
                if (WALL_LEFT == null)
                    WALL_LEFT = Image.load(COMMON_PATH + "left.png");
                yield WALL_LEFT;
            }
            case WallTypes.RIGHT -> {
                if (WALL_RIGHT == null)
                    WALL_RIGHT = Image.load(COMMON_PATH + "right.png");
                yield WALL_RIGHT;
            }
            case WallTypes.EXTERNAL_CORNER_BOTTOM_LEFT -> {
                if (WALL_EXTERNAL_CORNER_BOTTOM_LEFT == null)
                    WALL_EXTERNAL_CORNER_BOTTOM_LEFT = Image.load(COMMON_PATH + "externalCornerBottomLeft.png");
                yield WALL_EXTERNAL_CORNER_BOTTOM_LEFT;
            }
            case WallTypes.EXTERNAL_CORNER_BOTTOM_RIGHT -> {
                if (WALL_EXTERNAL_CORNER_BOTTOM_RIGHT == null)
                    WALL_EXTERNAL_CORNER_BOTTOM_RIGHT = Image.load(COMMON_PATH + "externalCornerBottomRight.png");
                yield WALL_EXTERNAL_CORNER_BOTTOM_RIGHT;
            }
            case WallTypes.EXTERNAL_CORNER_TOP_LEFT -> {
                if (WALL_EXTERNAL_CORNER_TOP_LEFT == null)
                    WALL_EXTERNAL_CORNER_TOP_LEFT = Image.load(COMMON_PATH + "externalCornerTopLeft.png");
                yield WALL_EXTERNAL_CORNER_TOP_LEFT;
            }
            case WallTypes.EXTERNAL_CORNER_TOP_RIGHT -> {
                if (WALL_EXTERNAL_CORNER_TOP_RIGHT == null)
                    WALL_EXTERNAL_CORNER_TOP_RIGHT = Image.load(COMMON_PATH + "externalCornerTopRight.png");
                yield WALL_EXTERNAL_CORNER_TOP_RIGHT;
            }
            default -> null;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        position.changeFirst((double) wall.getCol());
        position.changeSecond((double) wall.getRow());
        return position;
    }
}
