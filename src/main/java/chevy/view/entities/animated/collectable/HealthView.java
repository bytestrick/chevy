package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Health;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public final class HealthView extends AnimatedEntityView {
    private static final String HEALT_PATH = "/sprites/collectable/health/";
    private final Health health;

    public HealthView(Health health) {
        this.health = health;
        this.currentViewPosition = new Vector2<>((double) health.getCol(), (double) health.getRow());
        float offsetY = -0.5f;
        float duration = 2f;
        this.moveInterpolationY = new Interpolation(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, Interpolation.Type.EASE_OUT_EXPO);

        iniAnimation();
    }

    private void iniAnimation() {
        createAnimation(Health.State.IDLE, 0, 10, health.getState(Health.State.IDLE).getDuration(), HEALT_PATH +
                "idle", ".png");

        createAnimation(Health.State.COLLECTED, 0, 7, health.getState(Health.State.COLLECTED).getDuration(),
                HEALT_PATH + "collect", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = health.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (health.getCurrentState() == Health.State.COLLECTED) {
            moveInterpolationY.start();
        }
        currentViewPosition.second = moveInterpolationY.getValue();
        return currentViewPosition;
    }

    public void wasRemoved() {
        super.deleteAnimations();
        moveInterpolationY.delete();
    }
}