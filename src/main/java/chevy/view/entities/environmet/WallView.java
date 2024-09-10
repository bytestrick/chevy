package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.Wall;
import chevy.utils.Load;
import chevy.view.entities.EntityView;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public final class WallView extends EntityView {
    private static final String COMMON_PATH = "/sprites/chamberTiles/wallTiles/";
    private static BufferedImage top, bottom, left, right, topLeftCorner, topRightCorner,
            bottomLeftCorner, bottomRightCorner, cornerInteriorBottomRight,
            cornerInteriorBottomLeft, cornerInteriorTopRight, cornerInteriorTopLeft, topHole2,
            topBroken;
    private static BufferedImage topHole;
    private final Wall wall;
    private final Point2D.Double position = new Point2D.Double(0, 0);

    public WallView(Wall wall) {this.wall = wall;}

    @Override
    public BufferedImage getFrame() {
        return switch (wall.getType()) {
            case TOP -> {
                if (top == null) {
                    top = Load.image(COMMON_PATH + "top.png");
                }
                yield top;
            }
            case BOTTOM -> {
                if (bottom == null) {
                    bottom = Load.image(COMMON_PATH + "bottom.png");
                }
                yield bottom;
            }
            case LEFT -> {
                if (left == null) {
                    left = Load.image(COMMON_PATH + "left.png");
                }
                yield left;
            }
            case RIGHT -> {
                if (right == null) {
                    right = Load.image(COMMON_PATH + "right.png");
                }
                yield right;
            }
            case EXTERNAL_CORNER_BOTTOM_LEFT -> {
                if (bottomLeftCorner == null) {
                    bottomLeftCorner = Load.image(COMMON_PATH + "bottomLeftCorner.png");
                }
                yield bottomLeftCorner;
            }
            case EXTERNAL_CORNER_BOTTOM_RIGHT -> {
                if (bottomRightCorner == null) {
                    bottomRightCorner = Load.image(COMMON_PATH + "bottomRightCorner.png");
                }
                yield bottomRightCorner;
            }
            case EXTERNAL_CORNER_TOP_LEFT -> {
                if (topLeftCorner == null) {
                    topLeftCorner = Load.image(COMMON_PATH + "topLeftCorner.png");
                }
                yield topLeftCorner;
            }
            case EXTERNAL_CORNER_TOP_RIGHT -> {
                if (topRightCorner == null) {
                    topRightCorner = Load.image(COMMON_PATH + "topRightCorner.png");
                }
                yield topRightCorner;
            }
            case CORNER_INTERIOR_BOTTOM_RIGHT -> {
                if (cornerInteriorBottomRight == null) {
                    cornerInteriorBottomRight = Load.image(COMMON_PATH +
                            "interiorCornerBottomRight.png");
                }
                yield cornerInteriorBottomRight;
            }
            case CORNER_INTERIOR_BOTTOM_LEFT -> {
                if (cornerInteriorBottomLeft == null) {
                    cornerInteriorBottomLeft = Load.image(COMMON_PATH + "interiorCornerBottomLeft" +
                            ".png");
                }
                yield cornerInteriorBottomLeft;
            }
            case CORNER_INTERIOR_TOP_RIGHT -> {
                if (cornerInteriorTopRight == null) {
                    cornerInteriorTopRight = Load.image(COMMON_PATH + "interiorCornerTopRight.png");
                }
                yield cornerInteriorTopRight;
            }
            case CORNER_INTERIOR_TOP_LEFT -> {
                if (cornerInteriorTopLeft == null) {
                    cornerInteriorTopLeft = Load.image(COMMON_PATH + "interiorCornerTopLeft.png");
                }
                yield cornerInteriorTopLeft;
            }
            case TOP_HOLE_2 -> {
                if (topHole2 == null) {
                    topHole2 = Load.image(COMMON_PATH + "topHole2.png");
                }
                yield topHole2;
            }
            case TOP_BROKEN -> {
                if (topBroken == null) {
                    topBroken = Load.image(COMMON_PATH + "topBroken.png");
                }
                yield topBroken;
            }
            case TOP_HOLE -> {
                if (topHole == null) {
                    topHole = Load.image(COMMON_PATH + "topHole.png");
                }
                yield topHole;
            }
        };
    }

    @Override
    public Point2D.Double getViewPosition() {
        position.setLocation(wall.getPosition());
        return position;
    }
}
