package chevy.model.staticEntity.environment;

import chevy.utilz.Vector2;

public class Ground extends Environment {
    public Ground(Vector2<Integer> initVelocity) {
        super(initVelocity, OrientationTypes.NONE, EnvironmentTypes.GROUND);
    }
}
