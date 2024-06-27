package chevy.view.entityView;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.utilz.Vector2;
import chevy.view.Image;
import chevy.view.animation.AnimatedSprite;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityView {
    protected boolean flipped = false;


    public EntityView() {}


    public Vector2<Integer> getOffset() {
        return new Vector2<>(0, 0);
    }

    public float getScale() {
        return 1f;
    }

    public abstract BufferedImage getCurrentFrame();

    public abstract Vector2<Double> getCurrentPosition();
}
