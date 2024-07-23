package chevy.view.entityView;

import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

public abstract class EntityView {
    protected Vector2<Double> currentViewPosition;


    public EntityView() {}


    public Vector2<Integer> getOffset() {
        return new Vector2<>(0, 0);
    }

    public float getScale() {
        return 1f;
    }

    public abstract BufferedImage getCurrentFrame();

    public abstract Vector2<Double> getCurrentViewPosition();

    public void wasRemoved() {}
}
