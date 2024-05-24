package chevy.model.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class Ignition extends PowerUp {
    public Ignition(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.IGNITION);
    }
}
