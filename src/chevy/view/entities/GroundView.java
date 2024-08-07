package chevy.view.entities;

import chevy.model.entity.staticEntity.environment.Ground;
import chevy.utils.Image;
import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

public class GroundView extends EntityView {
    private static final String TILES_RESOURCES = "/assets/chamberTiles/groundTiles/";
    private static BufferedImage GROUND_CENTRAL = null;
    private static BufferedImage GROUND_LEFT = null;
    private static BufferedImage GROUND_RIGHT = null;
    private static BufferedImage GROUND_TOP = null;
    private static BufferedImage GROUND_INTERIOR_CORNER_TOP_LEFT = null;
    private static BufferedImage GROUND_INTERIOR_CORNER_TOP_RIGHT = null;
    private final Ground ground;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);

    public GroundView(Ground ground) {
        this.ground = ground;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return switch (ground.getSpecificType()) {
            case CENTRAL -> {
                if (GROUND_CENTRAL == null) {
                    GROUND_CENTRAL = Image.load(TILES_RESOURCES + "central.png");
                }
                yield GROUND_CENTRAL;
            }
            case LEFT -> {
                if (GROUND_LEFT == null) {
                    GROUND_LEFT = Image.load(TILES_RESOURCES + "left.png");
                }
                yield GROUND_LEFT;
            }
            case RIGHT -> {
                if (GROUND_RIGHT == null) {
                    GROUND_RIGHT = Image.load(TILES_RESOURCES + "right.png");
                }
                yield GROUND_RIGHT;
            }
            case TOP -> {
                if (GROUND_TOP == null) {
                    GROUND_TOP = Image.load(TILES_RESOURCES + "top.png");
                }
                yield GROUND_TOP;
            }
            case INTERIOR_CORNER_TOP_LEFT -> {
                if (GROUND_INTERIOR_CORNER_TOP_LEFT == null) {
                    GROUND_INTERIOR_CORNER_TOP_LEFT = Image.load(TILES_RESOURCES + "interiorCornerTopLeft.png");
                }
                yield GROUND_INTERIOR_CORNER_TOP_LEFT;
            }
            case INTERIOR_CORNER_TOP_RIGHT -> {
                if (GROUND_INTERIOR_CORNER_TOP_RIGHT == null) {
                    GROUND_INTERIOR_CORNER_TOP_RIGHT = Image.load(TILES_RESOURCES + "interiorCornerTopRight.png");
                }
                yield GROUND_INTERIOR_CORNER_TOP_RIGHT;
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