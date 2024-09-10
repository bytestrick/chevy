package chevy.view.entities.animated.environmet;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

abstract class EnvironmentView extends AnimatedEntityView {
    final Environment environment;
    private EntityState previousAnimationState;

    EnvironmentView(Environment environment) {
        this.environment = environment;
        viewPosition = new Point2D.Double(environment.getCol(), environment.getRow());
    }

    @Override
    public Point getOffset() {
        AnimatedSprite animatedSprite = getAnimatedSprite(environment.getState(), null);
        if (animatedSprite != null) {
            return animatedSprite.getOffset();
        }
        return super.getOffset();
    }

    @Override
    public BufferedImage getFrame() {
        final EntityState state = environment.getState();
        final AnimatedSprite animatedSprite = getAnimatedSprite(state, null);

        if (animatedSprite != null) {
            EntityState open = environment.getType() == Environment.Type.CHEST ?
                    Chest.State.OPEN : Stair.State.OPEN;
            if (animatedSprite.isNotRunning()) {
                if (previousAnimationState != open) {
                    animatedSprite.restart();
                } else if (state != open) {
                    animatedSprite.restart();
                }
            }
            previousAnimationState = state;
            return animatedSprite.getFrame();
        }
        return null;
    }
}
