package chevy.view.entityView.entityViewAnimated.collectable;

import chevy.model.entity.collectable.Coin;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class CoinView extends EntityViewAnimated {
    private static final String COIN_PATH = "/assets/collectable/coin/";
    private final Coin coin;
    private final Interpolate moveInterpolationY;


    public CoinView(Coin coin) {
        this.coin = coin;
        this.currentViewPosition = new Vector2<>((double) coin.getCol(), (double) coin.getRow());
        float offsetY = -0.5f;
        float duration = 2f;
        this.moveInterpolationY = new Interpolate(currentViewPosition.second, currentViewPosition.second + offsetY,
                duration, InterpolationTypes.EASE_OUT_EXPO);

        iniAnimation();
    }


    private void iniAnimation() {
        createAnimation(Coin.EnumState.IDLE, 0,
                6, coin.getState(Coin.EnumState.IDLE).getDuration(),
                COIN_PATH + "idle", ".png");

        createAnimation(Coin.EnumState.COLLECTED, 0,
                7, coin.getState(Coin.EnumState.COLLECTED).getDuration(),
                COIN_PATH + "collect", ".png");
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = coin.getCurrentEumState();
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
        if (coin.getCurrentEumState() == Coin.EnumState.COLLECTED)
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
