package chevy.model.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class Carrot extends PowerUp {
    public Carrot(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.CARROT);
    }
}
