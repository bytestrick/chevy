package chevy.model.entity.staticEntity.environment;

import chevy.utils.Vector2;

public class Barrier extends Environment {
    public Barrier(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.BARRIER);
    }
}