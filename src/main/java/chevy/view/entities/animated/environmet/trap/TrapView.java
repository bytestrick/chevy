package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

abstract class TrapView extends AnimatedEntityView {
    final Trap trap;

    TrapView(Trap trap) {
        this.trap = trap;
        viewPosition = new Vector2<>((double) trap.getCol(), (double) trap.getRow());
        initializeAnimation();
    }

    @Override
    public void remove() {deleteAnimations();}

    @Override
    protected Direction getAnimationDirection(EntityState state) {return null;}

    @Override
    public BufferedImage getFrame() {
        final AnimatedSprite animatedSprite = getAnimatedSprite(trap.getState(), null);
        if (animatedSprite != null) {
            if (!animatedSprite.isRunning()) {
                animatedSprite.restart();
            }
            return animatedSprite.getFrame();
        }
        return null;
    }

    @Override
    public Vector2<Integer> getOffset() {
        return getAnimatedSprite(trap.getState(), null).getOffset();
    }
}