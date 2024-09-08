package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.Coin;

public final class CoinView extends CollectableView {
    private static final String COIN_RES = "/sprites/collectable/coin/";

    public CoinView(Coin coin) {super(coin);}

    @Override
    protected void initializeAnimation() {
        final float idleDuration = collectable.getState(Coin.State.IDLE).getDuration();
        animate(Coin.State.IDLE, null, 6, idleDuration, COIN_RES + "idle");

        final float collectedDuration = collectable.getState(Coin.State.IDLE).getDuration();
        animate(Coin.State.COLLECTED, null, 7, collectedDuration, COIN_RES + "collect");
    }
}