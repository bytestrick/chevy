package chevy.model.entity.staticEntity.powerUp;

import chevy.utils.Vector2;

public class Ignition extends PowerUp {
    public Ignition(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.IGNITION);
    }
}
