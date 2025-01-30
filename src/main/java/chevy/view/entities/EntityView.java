package chevy.view.entities;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class EntityView {
    protected Point2D.Double viewPosition;

    public Point getOffset() {
        return new Point(0, 0);
    }

    public float getScale() {
        return 1f;
    }

    public abstract BufferedImage getFrame();

    public Point2D.Double getViewPosition() {
        return viewPosition;
    }

    public void remove() {
    }
}
