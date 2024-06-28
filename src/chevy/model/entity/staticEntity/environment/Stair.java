package chevy.model.entity.staticEntity.environment;

import chevy.utils.Vector2;

public class Stair extends Environment {
    public Stair(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.STAIR);
    }
}
