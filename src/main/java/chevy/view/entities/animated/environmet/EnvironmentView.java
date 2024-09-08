package chevy.view.entities.animated.environmet;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

abstract class EnvironmentView extends AnimatedEntityView {
    final Environment environment;
    private EntityState previousAnimationState;

    EnvironmentView(Environment environment) {
        this.environment = environment;
        viewPosition = new Vector2<>((double) environment.getCol(), (double) environment.getRow());
        initializeAnimation();
    }

    @Override
    protected Direction getAnimationDirection(EntityState state) {return null;}

    @Override
    public Vector2<Integer> getOffset() {
        AnimatedSprite currentAnimatedSprite = getAnimatedSprite(environment.getState(), null);
        if (currentAnimatedSprite != null) {
            return currentAnimatedSprite.getOffset();
        }
        return super.getOffset();
    }

    @Override
    public BufferedImage getFrame() {
        final EntityState state = environment.getState();
        final AnimatedSprite animatedSprite = getAnimatedSprite(state, null);
        if (animatedSprite != null) {
            if (!animatedSprite.isRunning()) {
                if (previousAnimationState != Environment.State.OPEN) {
                    animatedSprite.restart();
                } else if (state != Environment.State.OPEN) {
                    animatedSprite.restart();
                }
            }
            previousAnimationState = state;
            return animatedSprite.getFrame();
        }
        return null;
    }
}
