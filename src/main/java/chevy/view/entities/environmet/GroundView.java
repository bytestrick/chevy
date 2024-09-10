package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.Ground;
import chevy.utils.Load;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;

import java.awt.image.BufferedImage;

public final class GroundView extends EntityView {
    private static final String TILES_RESOURCES = "/sprites/chamberTiles/groundTiles/";
    private static BufferedImage CENTRAL = null;
    private static BufferedImage LEFT = null;
    private static BufferedImage RIGHT = null;
    private static BufferedImage TOP = null;
    private static BufferedImage INTERIOR_CORNER_TOP_LEFT = null;
    private static BufferedImage INTERIOR_CORNER_TOP_RIGHT = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT = null;
    private static BufferedImage EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT = null;
    private static BufferedImage EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_RIGHT = null;
    private static BufferedImage CENTRAL_PATTERNED_2 = null;
    private static BufferedImage CENTRAL_PATTERNED = null;
    private static BufferedImage CENTRAL_BROKEN_2 = null;
    private static BufferedImage CENTRAL_BROKEN = null;
    private static BufferedImage CENTRAL_BROKEN_3 = null;
    private static BufferedImage EXTERNAL_CORNER_BOTTOM_LEFT = null;
    private final Ground ground;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);

    public GroundView(Ground ground) {
        this.ground = ground;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return switch (ground.getSpecificType()) {
            case CENTRAL -> {
                if (CENTRAL == null) {
                    CENTRAL = Load.image(TILES_RESOURCES + "central.png");
                }
                yield CENTRAL;
            }
            case LEFT -> {
                if (LEFT == null) {
                    LEFT = Load.image(TILES_RESOURCES + "left.png");
                }
                yield LEFT;
            }
            case RIGHT -> {
                if (RIGHT == null) {
                    RIGHT = Load.image(TILES_RESOURCES + "right.png");
                }
                yield RIGHT;
            }
            case TOP -> {
                if (TOP == null) {
                    TOP = Load.image(TILES_RESOURCES + "top.png");
                }
                yield TOP;
            }
            case INTERIOR_CORNER_TOP_LEFT -> {
                if (INTERIOR_CORNER_TOP_LEFT == null) {
                    INTERIOR_CORNER_TOP_LEFT = Load.image(TILES_RESOURCES + "interiorCornerTopLeft.png");
                }
                yield INTERIOR_CORNER_TOP_LEFT;
            }
            case INTERIOR_CORNER_TOP_RIGHT -> {
                if (INTERIOR_CORNER_TOP_RIGHT == null) {
                    INTERIOR_CORNER_TOP_RIGHT = Load.image(TILES_RESOURCES + "interiorCornerTopRight.png");
                }
                yield INTERIOR_CORNER_TOP_RIGHT;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM -> {
                if (EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM == null) {
                    EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM = Load.image(TILES_RESOURCES + "externalCornerBottomRightSideBottom.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT -> {
                if (EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT == null) {
                    EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT = Load.image(TILES_RESOURCES + "externalCornerBottomRightSideRight.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM -> {
                if (EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM == null) {
                    EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM = Load.image(TILES_RESOURCES + "externalCornerBottomLeftSideBottom.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT -> {
                if (EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT == null) {
                    EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT = Load.image(TILES_RESOURCES + "externalCornerBottomLeftSideLeft.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT;
            }
            case EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT -> {
                if (EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT == null) {
                    EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT = Load.image(TILES_RESOURCES + "externalCornerTopRightSideRight.png");
                }
                yield EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT;
            }
            case EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT -> {
                if (EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT == null) {
                    EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT = Load.image(TILES_RESOURCES + "externalCornerTopLeftSideLeft.png");
                }
                yield EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT -> {
                if (EXTERNAL_CORNER_BOTTOM_RIGHT == null) {
                    EXTERNAL_CORNER_BOTTOM_RIGHT = Load.image(TILES_RESOURCES + "externalCornerBottomRight.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_RIGHT;
            }
            case CENTRAL_PATTERNED_2 -> {
                if (CENTRAL_PATTERNED_2 == null) {
                    CENTRAL_PATTERNED_2 = Load.image(TILES_RESOURCES + "centralPatterned2.png");
                }
                yield CENTRAL_PATTERNED_2;
            }
            case CENTRAL_PATTERNED -> {
                if (CENTRAL_PATTERNED == null) {
                    CENTRAL_PATTERNED = Load.image(TILES_RESOURCES + "centralPatterned.png");
                }
                yield CENTRAL_PATTERNED;
            }
            case CENTRAL_BROKEN_2 -> {
                if (CENTRAL_BROKEN_2 == null) {
                    CENTRAL_BROKEN_2 = Load.image(TILES_RESOURCES + "centralBroken3.png");
                }
                yield CENTRAL_BROKEN_2;
            }
            case CENTRAL_BROKEN -> {
                if (CENTRAL_BROKEN == null) {
                    CENTRAL_BROKEN = Load.image(TILES_RESOURCES + "centralBroken.png");
                }
                yield CENTRAL_BROKEN;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT -> {
                if (EXTERNAL_CORNER_BOTTOM_LEFT == null) {
                    EXTERNAL_CORNER_BOTTOM_LEFT = Load.image(TILES_RESOURCES + "externalCornerBottomLeft.png");
                }
                yield EXTERNAL_CORNER_BOTTOM_LEFT;
            }
            case CENTRAL_BROKEN_3 -> {
                if (CENTRAL_BROKEN_3 == null) {
                    CENTRAL_BROKEN_3 = Load.image(TILES_RESOURCES + "centralBroken3.png");
                }
                yield CENTRAL_BROKEN_3;
            }
            default -> null;
        };
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        position.changeFirst((double) ground.getCol());
        position.changeSecond((double) ground.getRow());
        return position;
    }
}