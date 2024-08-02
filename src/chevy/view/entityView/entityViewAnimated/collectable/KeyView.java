package chevy.view.entityView.entityViewAnimated.collectable;

import chevy.model.entity.collectable.Key;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class KeyView extends EntityViewAnimated {
    private static final String KEY_PATH = "/assets/collectable/key/";
    private final Key key;
    private final Interpolate moveInterpolationY;


    public KeyView(Key key) {
        this.key = key;
        this.currentViewPosition = new Vector2<>((double) key.getCol(), (double) key.getRow());
        float offsetY = -0.5f;
        float duration = 2f;
        this.moveInterpolationY = new Interpolate(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, InterpolationTypes.EASE_OUT_EXPO);

        iniAnimation();
    }


    private void iniAnimation() {
        createAnimation(Key.EnumState.IDLE, 0,
                12, key.getState(Key.EnumState.IDLE).getDuration(),
                KEY_PATH + "idle", ".png");

        createAnimation(Key.EnumState.COLLECTED, 0,
                7, key.getState(Key.EnumState.COLLECTED).getDuration(),
                KEY_PATH + "collect", ".png");
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = key.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, 0);

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
        if (key.getCurrentEumState().equals(Key.EnumState.COLLECTED))
            moveInterpolationY.start();
        currentViewPosition.second = moveInterpolationY.getValue();
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        super.deleteAnimations();
        moveInterpolationY.delete();
    }
}
