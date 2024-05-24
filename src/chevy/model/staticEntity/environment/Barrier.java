package chevy.model.staticEntity.environment;

import chevy.utilz.Vector2;

public class Barrier extends Environment {
    public Barrier(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, EnvironmentTypes.BARRIER);
    }
}
