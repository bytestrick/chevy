package chevy.model.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class CoinOfGreed extends PowerUp {
    public CoinOfGreed(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.COIN_OF_GREED);
    }
}
