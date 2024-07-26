package chevy.view.entityView.entityViewAnimated.collectable;

import chevy.model.entity.collectable.Coin;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.utils.Vector2;
import chevy.view.Image;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class HealthView extends EntityViewAnimated {
    private static final String HEALT_PATH = "/assets/collectable/health/";
    private final Interpolate moveInterpolationY;
    private final Health health;


    public HealthView(Health health) {
        this.health = health;
        this.currentViewPosition = new Vector2<>((double) health.getCol(), (double) health.getRow());
        float offsetY = -0.5f;
        float duration = 2f;
        this.moveInterpolationY = new Interpolate(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, InterpolationTypes.EASE_OUT_EXPO);

        iniAnimation();
    }


    private void iniAnimation() {
        createAnimation(Health.EnumState.IDLE, 0,
                10, health.getState(Health.EnumState.IDLE).getDuration(),
                HEALT_PATH + "idle", ".png");

        createAnimation(Health.EnumState.COLLECT, 0,
                7, health.getState(Health.EnumState.COLLECT).getDuration(),
                HEALT_PATH + "collect", ".png");
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentStateState = health.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentStateState, 0);

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
        if (health.getCurrentEumState() == Health.EnumState.COLLECT)
            moveInterpolationY.start();
        currentViewPosition.second = moveInterpolationY.getValue();
        return currentViewPosition;
    }

    public void wasRemoved() {
        moveInterpolationY.delete();
    }
}
