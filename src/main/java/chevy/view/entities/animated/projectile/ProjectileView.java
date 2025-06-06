package chevy.view.entities.animated.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

abstract class ProjectileView extends AnimatedEntityView {
    final Projectile projectile;

    ProjectileView(Projectile projectile) {
        this.projectile = projectile;
        viewPosition = new Point2D.Double(projectile.getCol(), projectile.getRow());
        vertex = projectile.getState(projectile.getState());

        assert vertex != null;
        float duration = vertex.getDuration();
        horizontal = new Interpolation(viewPosition.x, viewPosition.x, duration, Interpolation.Type.LINEAR);
        vertical = new Interpolation(viewPosition.y, viewPosition.y, duration, Interpolation.Type.LINEAR);
    }

    @Override
    public Point2D.Double getViewPosition() {
        if (vertex.isFinished()) {
            vertex = projectile.getState(projectile.getState());
            firstTimeInState = true;
        } else if (firstTimeInState && vertex.getState() != Projectile.State.END) {
            float duration = vertex.getDuration();
            horizontal.changeStart(viewPosition.x);
            horizontal.changeEnd(projectile.getCol());
            horizontal.changeDuration(duration);
            horizontal.restart();
            vertical.changeStart(viewPosition.y);
            vertical.changeEnd(projectile.getRow());
            vertical.changeDuration(duration);
            vertical.restart();
            firstTimeInState = false;
        }

        viewPosition.x = horizontal.getValue();
        viewPosition.y = vertical.getValue();
        return viewPosition;
    }

    public float getScale() {
        EntityState state = projectile.getState();
        Direction direction = getAnimationDirection(state);
        AnimatedSprite currentAnimatedSprite = getAnimatedSprite(state, direction);
        return currentAnimatedSprite.getScale();
    }

    public Point getOffset() {
        EntityState state = projectile.getState();
        Direction direction = getAnimationDirection(state);
        AnimatedSprite currentAnimatedSprite = getAnimatedSprite(state, direction);
        return currentAnimatedSprite.getOffset();
    }

    private Direction getAnimationDirection(EntityState state) {
        return switch (state) {
            case Projectile.State.START, Projectile.State.LOOP, Projectile.State.END -> projectile.getDirection();
            default -> null;
        };
    }

    @Override
    public BufferedImage getFrame() {
        EntityState state = projectile.getState();
        Direction direction = projectile.getDirection();
        AnimatedSprite animatedSprite = getAnimatedSprite(state, direction);
        if (animatedSprite != null) {
            if (animatedSprite.isNotRunning()) {
                animatedSprite.restart();
            } else if (state == Projectile.State.END && vertex.isFinished()) {
                projectile.setShouldDraw(false);
            }
            return animatedSprite.getFrame();
        }
        return null;
    }
}
