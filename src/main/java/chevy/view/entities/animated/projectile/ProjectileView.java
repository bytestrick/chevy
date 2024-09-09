package chevy.view.entities.animated.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.stateMachine.EntityState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

abstract class ProjectileView extends AnimatedEntityView {
    final Projectile projectile;

    ProjectileView(Projectile projectile) {
        this.projectile = projectile;
        viewPosition = new Vector2<>((double) projectile.getCol(), (double) projectile.getRow());
        vertex = projectile.getState(projectile.getState());

        assert vertex != null;
        final float duration = vertex.getDuration();
        horizontal = new Interpolation(viewPosition.first, viewPosition.first, duration, Interpolation.Type.LINEAR);
        vertical = new Interpolation(viewPosition.second, viewPosition.second, duration, Interpolation.Type.LINEAR);
        initializeAnimation();
    }

    @Override
    public Vector2<Double> getViewPosition() {
        if (vertex.isFinished()) {
            vertex = projectile.getState(projectile.getState());
            firstTimeInState = true;
        } else if (firstTimeInState && vertex.getState() != Projectile.State.END) {
            float duration = vertex.getDuration();
            horizontal.changeStart(viewPosition.first);
            horizontal.changeEnd(projectile.getCol());
            horizontal.changeDuration(duration);
            horizontal.restart();
            vertical.changeStart(viewPosition.second);
            vertical.changeEnd(projectile.getRow());
            vertical.changeDuration(duration);
            vertical.restart();
            firstTimeInState = false;
        }

        viewPosition.first = horizontal.getValue();
        viewPosition.second = vertical.getValue();
        return viewPosition;
    }

    public float getScale() {
        final EntityState state = projectile.getState();
        final Direction direction = getAnimationDirection(state);
        final AnimatedSprite currentAnimatedSprite = getAnimatedSprite(state, direction);
        return currentAnimatedSprite.getScale();
    }

    private Direction getAnimationDirection(EntityState state) {
        return switch (state) {
            case Projectile.State.START, Projectile.State.LOOP, Projectile.State.END -> projectile.getDirection();
            default -> null;
        };
    }

    @Override
    public BufferedImage getFrame() {
        final EntityState state = projectile.getState();
        final Direction direction = projectile.getDirection();
        final AnimatedSprite currentAnimatedSprite = getAnimatedSprite(state, direction);
        if (currentAnimatedSprite != null) {
            if (state == Projectile.State.END && vertex.isFinished()) {
                projectile.setShouldDraw(false);
            } else if (currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getFrame();
        }
        return null;
    }
}