package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Coin;

public final class CoinView extends CollectableView {
    public CoinView(Coin coin) {
        super(coin);

        String res = "/sprites/collectable/coin/";
        float idleDuration = collectable.getState(Coin.State.IDLE).getDuration();
        animate(Coin.State.IDLE, null, 6, idleDuration, res + "idle");

        float collectedDuration = collectable.getState(Coin.State.IDLE).getDuration();
        animate(Coin.State.COLLECTED, null, 7, collectedDuration, res + "collect");
    }
}
