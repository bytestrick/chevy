package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Coin;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public final class CoinView extends AnimatedEntityView {
    private static final String COIN_PATH = "/sprites/collectable/coin/";
    private final Coin coin;

    public CoinView(Coin coin) {
        this.coin = coin;
        this.currentViewPosition = new Vector2<>((double) coin.getCol(), (double) coin.getRow());
        float offsetY = -0.5f;
        float duration = 2f;
        this.moveInterpolationY = new Interpolation(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, Interpolation.Type.EASE_OUT_EXPO);

        iniAnimation();
    }

    private void iniAnimation() {
        createAnimation(Coin.State.IDLE, 0, 6, coin.getState(Coin.State.IDLE).getDuration(), COIN_PATH + "idle", 
                ".png");

        createAnimation(Coin.State.COLLECTED, 0, 7, coin.getState(Coin.State.COLLECTED).getDuration(), COIN_PATH + 
                "collect", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = coin.getCurrentState();
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
        if (coin.getCurrentState() == Coin.State.COLLECTED) {
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