package chevy.model.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class BrokenArrows extends PowerUp {
    public BrokenArrows(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.BROKEN_ARROWS);
    }
}
