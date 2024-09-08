package chevy.view.entities;

import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

public abstract class EntityView {
    protected Vector2<Double> viewPosition;

    public Vector2<Integer> getOffset() {return new Vector2<>(0, 0);}

    public float getScale() {return 1f;}

    public abstract BufferedImage getFrame();

    public Vector2<Double> getViewPosition() {return viewPosition;}

    public void remove() {}
}