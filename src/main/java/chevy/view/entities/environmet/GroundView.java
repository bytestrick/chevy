package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.Ground;
import chevy.utils.Load;
import chevy.view.entities.EntityView;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public final class GroundView extends EntityView {
    private static final String RES = "/sprites/chamberTiles/groundTiles/";
    private static BufferedImage middle, left, right, top, topLeftInnerCorner,
            topRightInnerCorner, externalCornerBottomRightSideBottom,
            externalCornerBottomRightSideRight, externalCornerBottomLeftSideBottom,
            externalCornerBottomLeftSideLeft, externalCornerTopRightSideRight,
            externalCornerTopLeftSideLeft, externalCornerBottomRight, centralPatterned2,
            centralPatterned, centralBroken2, centralBroken, centralBroken3,
            externalCornerBottomLeft;
    private final Ground ground;
    private final Point2D.Double position = new Point2D.Double(0, 0);

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
            case EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM -> {
                if (externalCornerBottomRightSideBottom == null) {
                    externalCornerBottomRightSideBottom = Load.image(RES +
                            "externalCornerBottomRightSideBottom.png");
                }
                yield externalCornerBottomRightSideBottom;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT -> {
                if (externalCornerBottomRightSideRight == null) {
                    externalCornerBottomRightSideRight = Load.image(RES +
                            "externalCornerBottomRightSideRight.png");
                }
                yield externalCornerBottomRightSideRight;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM -> {
                if (externalCornerBottomLeftSideBottom == null) {
                    externalCornerBottomLeftSideBottom = Load.image(RES +
                            "externalCornerBottomLeftSideBottom.png");
                }
                yield externalCornerBottomLeftSideBottom;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT -> {
                if (externalCornerBottomLeftSideLeft == null) {
                    externalCornerBottomLeftSideLeft = Load.image(RES +
                            "externalCornerBottomLeftSideLeft.png");
                }
                yield externalCornerBottomLeftSideLeft;
            }
            case EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT -> {
                if (externalCornerTopRightSideRight == null) {
                    externalCornerTopRightSideRight = Load.image(RES +
                            "externalCornerTopRightSideRight.png");
                }
                yield externalCornerTopRightSideRight;
            }
            case EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT -> {
                if (externalCornerTopLeftSideLeft == null) {
                    externalCornerTopLeftSideLeft = Load.image(RES +
                            "externalCornerTopLeftSideLeft.png");
                }
                yield externalCornerTopLeftSideLeft;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT -> {
                if (externalCornerBottomRight == null) {
                    externalCornerBottomRight = Load.image(RES + "externalCornerBottomRight.png");
                }
                yield externalCornerBottomRight;
            }
            case CENTRAL_PATTERNED_2 -> {
                if (centralPatterned2 == null) {
                    centralPatterned2 = Load.image(RES + "centralPatterned2.png");
                }
                yield centralPatterned2;
            }
            case CENTRAL_PATTERNED -> {
                if (centralPatterned == null) {
                    centralPatterned = Load.image(RES + "centralPatterned.png");
                }
                yield centralPatterned;
            }
            case CENTRAL_BROKEN_2 -> {
                if (centralBroken2 == null) {
                    centralBroken2 = Load.image(RES + "centralBroken3.png");
                }
                yield centralBroken2;
            }
            case CENTRAL_BROKEN -> {
                if (centralBroken == null) {
                    centralBroken = Load.image(RES + "centralBroken.png");
                }
                yield centralBroken;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT -> {
                if (externalCornerBottomLeft == null) {
                    externalCornerBottomLeft = Load.image(RES + "externalCornerBottomLeft.png");
                }
                yield externalCornerBottomLeft;
            }
            case CENTRAL_BROKEN_3 -> {
                if (centralBroken3 == null) {
                    centralBroken3 = Load.image(RES + "centralBroken3.png");
                }
                yield centralBroken3;
            }
        };
    }

    @Override
    public Point2D.Double getViewPosition() {
        position.setLocation(ground.getPosition());
        return position;
    }
}
