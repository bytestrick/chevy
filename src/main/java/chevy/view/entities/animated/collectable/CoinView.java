package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Coin;

public final class CoinView extends CollectableView {
    public CoinView(Coin coin) {
        super(coin);

        final String res = "/sprites/collectable/coin/";
        final float idleDuration = collectable.getState(Coin.State.IDLE).getDuration();
        animate(Coin.State.IDLE, null, 6, idleDuration, res + "idle");

        final float collectedDuration = collectable.getState(Coin.State.IDLE).getDuration();
        animate(Coin.State.COLLECTED, null, 7, collectedDuration, res + "collect");
    }
}
