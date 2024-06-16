package chevy.model.entity.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class Ignition extends PowerUp {
    public Ignition(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.IGNITION);
    }
}
