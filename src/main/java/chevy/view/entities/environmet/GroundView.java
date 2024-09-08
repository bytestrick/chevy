package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.Ground;
import chevy.utils.Load;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;

import java.awt.image.BufferedImage;

public final class GroundView extends EntityView {
    private static final String RES = "/sprites/chamberTiles/groundTiles/";
    private static BufferedImage middle, left, right, top, topLeftInnerCorner, topRightInnerCorner;
    private final Ground ground;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);

    public GroundView(Ground ground) {this.ground = ground;}

    @Override
    public BufferedImage getFrame() {
        return switch (ground.getType()) {
            case CENTRAL -> {
                if (middle == null) {
                    middle = Load.image(RES + "middle.png");
                }
                yield middle;
            }
            case LEFT -> {
                if (left == null) {
                    left = Load.image(RES + "left.png");
                }
                yield left;
            }
            case RIGHT -> {
                if (right == null) {
                    right = Load.image(RES + "right.png");
                }
                yield right;
            }
            case TOP -> {
                if (top == null) {
                    top = Load.image(RES + "top.png");
                }
                yield top;
            }
            case INTERIOR_CORNER_TOP_LEFT -> {
                if (topLeftInnerCorner == null) {
                    topLeftInnerCorner = Load.image(RES + "topLeftInnerCorner.png");
                }
                yield topLeftInnerCorner;
            }
            case INTERIOR_CORNER_TOP_RIGHT -> {
                if (topRightInnerCorner == null) {
                    topRightInnerCorner = Load.image(RES + "topRightInnerCorner.png");
                }
                yield topRightInnerCorner;
            }
            default -> null;
        };
    }

    @Override
    public Vector2<Double> getViewPosition() {
        position.changeFirst((double) ground.getCol());
        position.changeSecond((double) ground.getRow());
        return position;
    }
}