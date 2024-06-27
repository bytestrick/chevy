package chevy.view.entityView.entityViewAnimated;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.utilz.Pair;
import chevy.utilz.Vector2;
import chevy.view.Image;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.chamber.EntityToEntityView;
import chevy.view.entityView.EntityView;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityViewAnimated extends EntityView {
    private final Map<Pair<CommonEnumStates, Integer>, AnimatedSprite> animations;

    public EntityViewAnimated() {
        animations = new HashMap<>();
    }


    protected void initAnimation(AnimatedSprite animation, String folderPath, String extension) {
        for (int i = 0; i < animation.getNFrame(); ++i)
            animation.addFrame(i, Image.load(folderPath + "/" + i + extension));
        addAnimation(animation.getAnimationTypes(), animation);
    }

    protected void addAnimation(Pair<CommonEnumStates, Integer> animationTypes, AnimatedSprite animatedSprite) {
        if (!animations.containsKey(animationTypes))
            animations.put(animationTypes, animatedSprite);
        else
            System.out.println("[!] L'animaizone " + animationTypes + " è già presente");
    }

    protected void deleteAnimations() {
        for (Pair<CommonEnumStates, Integer> key : animations.keySet()) {
            animations.get(key).delete();
        }
    }

    protected AnimatedSprite getAnimatedSprite(CommonEnumStates enumStates, int type) {
        Pair<CommonEnumStates, Integer> animationTypes = new Pair<>(enumStates, type);
        return animations.get(animationTypes);
    }

    public abstract BufferedImage getCurrentFrame();

    public abstract Vector2<Double> getCurrentPosition();

    public abstract void wasRemoved();
}
