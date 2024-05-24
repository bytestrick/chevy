package chevy.model.staticEntity.environment;

import chevy.utilz.Vector2;

public class Stair extends Environment {
    public Stair(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, EnvironmentTypes.STAIR);
    }
}
