package chevy.view.entityView.entityViewAnimated.collectable.powerUp;

import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class PowerUpView extends EntityViewAnimated {
    private static final String POWER_UP_PATH = "/assets/collectable/powerUp/";
    private final Interpolate moveInterpolationY;
    private CommonEnumStates previousAnimationState = null;
    private final PowerUp powerUp;


    public PowerUpView(PowerUp powerUp) {
        this.powerUp = powerUp;
        this.currentViewPosition = new Vector2<>((double) powerUp.getCol(), (double) powerUp.getRow());
        float offsetY = -0.5f;
        float duration = 1f;
        this.moveInterpolationY = new Interpolate(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, InterpolationTypes.EASE_OUT_EXPO);

        iniAnimation();
    }


    private void iniAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -6);
        createAnimation(PowerUp.EnumState.IDLE, 0,
                3, powerUp.getState(PowerUp.EnumState.IDLE).getDuration(),
                offset, 1,
                POWER_UP_PATH + "idle", ".png");

        createAnimation(PowerUp.EnumState.SELECTED, 0,
                4, powerUp.getState(PowerUp.EnumState.SELECTED).getDuration(),
                offset, 1,
                POWER_UP_PATH + "selected", ".png");

        createAnimation(PowerUp.EnumState.DESELECTED, 0,
                4, powerUp.getState(PowerUp.EnumState.DESELECTED).getDuration(),
                offset, 1,
                POWER_UP_PATH + "deselected", ".png");

        createAnimation(PowerUp.EnumState.COLLECTED, 0,
                7, powerUp.getState(PowerUp.EnumState.COLLECTED).getDuration(),
                offset, 1,
                POWER_UP_PATH + "collected", ".png");
    }


    @Override
    public Vector2<Integer> getOffset() {
        CommonEnumStates currentStateState = powerUp.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentStateState, 0);
        if (currentAnimatedSprite != null)
            return currentAnimatedSprite.getOffset();
        return new Vector2<>(0, 0);
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = powerUp.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                if (previousAnimationState != PowerUp.EnumState.SELECTED) {
                    currentAnimatedSprite.restart();
                }
                else if (currentEnumState != PowerUp.EnumState.SELECTED)
                    currentAnimatedSprite.restart();
            }
            previousAnimationState = currentEnumState;
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (powerUp.getCurrentEumState() == PowerUp.EnumState.COLLECTED)
            moveInterpolationY.start();

        currentViewPosition.second = moveInterpolationY.getValue();
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        super.deleteAnimations();
        this.moveInterpolationY.delete();
    }
}
