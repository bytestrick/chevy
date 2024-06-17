package chevy.view.entityView;

import chevy.model.entity.dinamicEntity.stateMachine.EnumState;
import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;
import chevy.view.Image;
import chevy.view.animation.AnimatedSprite;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityView {
    private final Map<EnumState, AnimatedSprite> animations;

    public EntityView() {
        animations = new HashMap<>();
    }


    protected void initAnimation(AnimatedSprite animation, String folderPath, String extension) {
        for (int i = 0; i < animation.getNFrame(); ++i)
            animation.addFrame(i, Image.loadImage(folderPath + "/" + i + extension));
        this.addAnimation(animation.getAnimationTypes(), animation);
    }

    protected void addAnimation(EnumState animationTypes, AnimatedSprite animatedSprite) {
        if (!animations.containsKey(animationTypes))
            animations.put(animationTypes, animatedSprite);
        else
            System.out.println("L'animaizone " + animationTypes + " è già presente");
    }

    protected AnimatedSprite getAnimatedSprite(EnumState animationTypes) {
        return animations.get(animationTypes);
    }

    public abstract BufferedImage getCurrentFrame();
}
