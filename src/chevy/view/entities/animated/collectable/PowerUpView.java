package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class PowerUpView extends AnimatedEntityView {
    private static final String POWER_UP_PATH = "/assets/collectable/powerUp/";
    private final Interpolation moveInterpolationY;
    private final PowerUp powerUp;
    private CommonState previousAnimationState = null;

    public PowerUpView(PowerUp powerUp) {
        this.powerUp = powerUp;
        this.currentViewPosition = new Vector2<>((double) powerUp.getCol(), (double) powerUp.getRow());
        float offsetY = -0.5f;
        float duration = 1f;
        this.moveInterpolationY = new Interpolation(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, Interpolation.Type.EASE_OUT_EXPO);

        iniAnimation();
    }

    private void iniAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -6);
        createAnimation(PowerUp.State.IDLE, 0, 3, powerUp.getState(PowerUp.State.IDLE).getDuration(), offset,
                1, POWER_UP_PATH + "idle", ".png");

        createAnimation(PowerUp.State.SELECTED, 0, 4, powerUp.getState(PowerUp.State.SELECTED).getDuration(),
                offset, 1, POWER_UP_PATH + "selected", ".png");

        createAnimation(PowerUp.State.DESELECTED, 0, 4,
                powerUp.getState(PowerUp.State.DESELECTED).getDuration(), offset, 1, POWER_UP_PATH + "deselected"
                , ".png");

        createAnimation(PowerUp.State.COLLECTED, 0, 7,
                powerUp.getState(PowerUp.State.COLLECTED).getDuration(), offset, 1, POWER_UP_PATH + "collected",
                ".png");
    }

    @Override
    public Vector2<Integer> getOffset() {
        CommonState currentStateState = powerUp.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentStateState, 0);
        if (currentAnimatedSprite != null) {
            return currentAnimatedSprite.getOffset();
        }
        return new Vector2<>(0, 0);
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentEnumState = powerUp.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                if (previousAnimationState != PowerUp.State.SELECTED) {
                    currentAnimatedSprite.restart();
                } else if (currentEnumState != PowerUp.State.SELECTED) {
                    currentAnimatedSprite.restart();
                }
            }
            previousAnimationState = currentEnumState;
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (powerUp.getCurrentState() == PowerUp.State.COLLECTED) {
            moveInterpolationY.start();
        }
        currentViewPosition.second = moveInterpolationY.getValue();
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        super.deleteAnimations();
        this.moveInterpolationY.delete();
    }
}