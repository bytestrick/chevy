package chevy.model.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class GoldArrow extends PowerUp {
    public GoldArrow(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.GOLD_ARROW);
    }
}
