package chevy.view.entities.animated;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public abstract class LiveEntityView extends AnimatedEntityView {
    protected final LiveEntity entity;

    protected LiveEntityView(LiveEntity entity) {
        this.entity = entity;
        viewPosition = new Point2D.Double(entity.getCol(), entity.getRow());
        vertex = entity.getState(entity.getState());

        final float duration = vertex.getDuration();
        horizontal = new Interpolation(viewPosition.x, viewPosition.x, duration,
                Interpolation.Type.EASE_OUT_SINE);
        vertical = new Interpolation(viewPosition.y, viewPosition.y, duration,
                Interpolation.Type.EASE_OUT_SINE);

        initializeAnimation();
    }

    @Override
    public Point2D.Double getViewPosition() {
        if (vertex.isFinished()) {
            vertex = entity.getState(entity.getState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = vertex.getDuration();
            horizontal.changeStart(viewPosition.x);
            horizontal.changeEnd(entity.getCol());
            horizontal.changeDuration(duration);
            horizontal.restart();
            vertical.changeStart(viewPosition.y);
            vertical.changeEnd(entity.getRow());
            vertical.changeDuration(duration);
            vertical.restart();
            firstTimeInState = false;
        }

        viewPosition.x = horizontal.getValue();
        viewPosition.y = vertical.getValue();
        return viewPosition;
    }

    @Override
    public BufferedImage getFrame() {
        EntityState currentState = entity.getState();
        Direction direction = getAnimationDirection(currentState);
        AnimatedSprite animatedSprite = getAnimatedSprite(currentState, direction);

        if (animatedSprite != null) {
            if (animatedSprite.isNotRunning()) {
                animatedSprite.restart();
            }
            return animatedSprite.getFrame();
        }
        return null;
    }

    @Override
    public Point getOffset() {
        final EntityState state = entity.getState();
        final Direction direction = getAnimationDirection(state);
        final AnimatedSprite animatedSprite = getAnimatedSprite(state, direction);
        assert animatedSprite != null : entity + ": " + state + ", " + direction;
        return animatedSprite.getOffset();
    }

    protected Direction getAnimationDirection(EntityState state) {
        final Direction direction = entity.getDirection();
        return switch (state) {
            case Enemy.State.ATTACK, Enemy.State.IDLE, Enemy.State.MOVE, Enemy.State.HIT ->
                    direction;
            case Enemy.State.DEAD -> direction == Direction.LEFT ? direction : Direction.RIGHT;
            default -> null;
        };
    }
}