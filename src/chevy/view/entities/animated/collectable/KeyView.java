package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Key;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class KeyView extends AnimatedEntityView {
    private static final String KEY_PATH = "/assets/collectable/key/";
    private final Key key;

    public KeyView(Key key) {
        this.key = key;
        this.currentViewPosition = new Vector2<>((double) key.getCol(), (double) key.getRow());
        float offsetY = -0.5f;
        float duration = 2f;
        this.moveInterpolationY = new Interpolation(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, Interpolation.Type.EASE_OUT_EXPO);

        iniAnimation();
    }

    private void iniAnimation() {
        createAnimation(Key.State.IDLE, 0, 12, key.getState(Key.State.IDLE).getDuration(), KEY_PATH + "idle", ".png");

        createAnimation(Key.State.COLLECTED, 0, 7, key.getState(Key.State.COLLECTED).getDuration(), KEY_PATH +
                "collect", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = key.getCurrentState();
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
        if (key.getCurrentState().equals(Key.State.COLLECTED)) {
            moveInterpolationY.start();
        }
        currentViewPosition.second = moveInterpolationY.getValue();
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        super.deleteAnimations();
        moveInterpolationY.delete();
    }
}