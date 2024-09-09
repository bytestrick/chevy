package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.stateMachine.EntityState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

abstract class CollectableView extends AnimatedEntityView {
    final Collectable collectable;

    CollectableView(Collectable collectable) {
        this.collectable = collectable;

        viewPosition = new Vector2<>((double) collectable.getCol(),
                (double) collectable.getRow());
        float offsetY = -0.5f;
        float duration = 2f;
        vertical = new Interpolation(viewPosition.second, viewPosition.second + offsetY, duration, Interpolation.Type.EASE_OUT_EXPO);

        initializeAnimation();
    }

    @Override
    public void remove() {
        deleteAnimations();
        vertical.delete();
    }

    @Override
    public Vector2<Double> getViewPosition() {
        if (collectable.getState().equals(Collectable.State.COLLECTED)) {
            vertical.start();
        }
        viewPosition.second = vertical.getValue();
        return viewPosition;
    }

    @Override
    public BufferedImage getFrame() {
        EntityState currentState = collectable.getState();
        AnimatedSprite currentAnimatedSprite = getAnimatedSprite(currentState, null);

        if (currentAnimatedSprite != null) {
            if (currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getFrame();
        }
        return null;
    }
}