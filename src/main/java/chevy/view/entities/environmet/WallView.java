package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.Wall;
import chevy.utils.Load;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;

import java.awt.image.BufferedImage;

public final class WallView extends EntityView {
    private static final String COMMON_PATH = "/sprites/chamberTiles/wallTiles/";
    private static BufferedImage TOP = null;
    private static BufferedImage BOTTOM = null;
    private static BufferedImage LEFT = null;
    private static BufferedImage RIGHT = null;
    private static BufferedImage EXTERNAL_CORNER_TOP_LEFT = null;
    private static BufferedImage EXTERNAL_CORNER_TOP_RIGHT = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_LEFT = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_RIGHT = null;
    private static BufferedImage CORNER_INTERIOR_BOTTOM_RIGHT = null;
    private static BufferedImage CORNER_INTERIOR_BOTTOM_LEFT = null;
    private static BufferedImage CORNER_INTERIOR_TOP_RIGHT = null;
    private static BufferedImage CORNER_INTERIOR_TOP_LEFT = null;
    private static BufferedImage TOP_HOLE_2 = null;
    private static BufferedImage TOP_BROKEN = null;
    private static BufferedImage TOP_HOLE = null;

    private final Wall wall;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);

    public WallView(Wall wall) {
        this.wall = wall;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return switch (wall.getSpecificType()) {
            case TOP -> {
                if (TOP == null) {
                    TOP = Load.image(COMMON_PATH + "top.png");
                }
                yield TOP;
            }
            case BOTTOM -> {
                if (BOTTOM == null) {
                    BOTTOM = Load.image(COMMON_PATH + "bottom.png");
                }
                yield BOTTOM;
            }
            case LEFT -> {
                if (LEFT == null) {
                    LEFT = Load.image(COMMON_PATH + "left.png");
                }
                yield LEFT;
            }
            case RIGHT -> {
                if (RIGHT == null) {
                    RIGHT = Load.image(COMMON_PATH + "right.png");
                }
                yield RIGHT;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT -> {
                if (EXTERNAL_CORNER_BOTTOM_LEFT == null) {
                    EXTERNAL_CORNER_BOTTOM_LEFT = Load.image(COMMON_PATH + "externalCornerBottomLeft.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_LEFT;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT -> {
                if (EXTERNAL_CORNER_BOTTOM_RIGHT == null) {
                    EXTERNAL_CORNER_BOTTOM_RIGHT = Load.image(COMMON_PATH + "externalCornerBottomRight.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_RIGHT;
            }
            case EXTERNAL_CORNER_TOP_LEFT -> {
                if (EXTERNAL_CORNER_TOP_LEFT == null) {
                    EXTERNAL_CORNER_TOP_LEFT = Load.image(COMMON_PATH + "externalCornerTopLeft.png");
                }
                yield EXTERNAL_CORNER_TOP_LEFT;
            }
            case EXTERNAL_CORNER_TOP_RIGHT -> {
                if (EXTERNAL_CORNER_TOP_RIGHT == null) {
                    EXTERNAL_CORNER_TOP_RIGHT = Load.image(COMMON_PATH + "externalCornerTopRight.png");
                }
                yield EXTERNAL_CORNER_TOP_RIGHT;
            }
            case CORNER_INTERIOR_BOTTOM_RIGHT -> {
                if (CORNER_INTERIOR_BOTTOM_RIGHT == null) {
                    CORNER_INTERIOR_BOTTOM_RIGHT = Load.image(COMMON_PATH + "interiorCornerBottomRight.png");
                }
                yield CORNER_INTERIOR_BOTTOM_RIGHT;
            }
            case CORNER_INTERIOR_BOTTOM_LEFT -> {
                if (CORNER_INTERIOR_BOTTOM_LEFT == null) {
                    CORNER_INTERIOR_BOTTOM_LEFT = Load.image(COMMON_PATH + "interiorCornerBottomLeft.png");
                }
                yield CORNER_INTERIOR_BOTTOM_LEFT;
            }
            case CORNER_INTERIOR_TOP_RIGHT -> {
                if (CORNER_INTERIOR_TOP_RIGHT == null) {
                    CORNER_INTERIOR_TOP_RIGHT = Load.image(COMMON_PATH + "interiorCornerTopRight.png");
                }
                yield CORNER_INTERIOR_TOP_RIGHT;
            }
            case CORNER_INTERIOR_TOP_LEFT -> {
                if (CORNER_INTERIOR_TOP_LEFT == null) {
                    CORNER_INTERIOR_TOP_LEFT = Load.image(COMMON_PATH + "interiorCornerTopLeft.png");
                }
                yield CORNER_INTERIOR_TOP_LEFT;
            }
            case TOP_HOLE_2 -> {
                if (TOP_HOLE_2 == null) {
                    TOP_HOLE_2 = Load.image(COMMON_PATH + "topHole2.png");
                }
                yield TOP_HOLE_2;
            }
            case TOP_BROKEN -> {
                if (TOP_BROKEN == null) {
                    TOP_BROKEN = Load.image(COMMON_PATH + "topBroken.png");
                }
                yield TOP_BROKEN;
            }
            case TOP_HOLE -> {
                if (TOP_HOLE == null) {
                    TOP_HOLE = Load.image(COMMON_PATH + "topHole.png");
                }
                yield TOP_HOLE;
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