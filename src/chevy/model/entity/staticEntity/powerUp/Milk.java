package chevy.model.entity.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class Milk extends PowerUp {
    public Milk(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.MILK);
    }
}
