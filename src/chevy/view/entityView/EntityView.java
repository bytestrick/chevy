package chevy.view.entityView;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.utilz.Vector2;
import chevy.view.Image;
import chevy.view.animation.AnimatedSprite;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityView {

    public EntityView() {}


    public abstract BufferedImage getCurrentFrame();

    public abstract Vector2<Double> getCurrentPosition();
}
