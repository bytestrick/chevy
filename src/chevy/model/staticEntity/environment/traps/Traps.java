package chevy.model.staticEntity.environment.traps;

import chevy.model.staticEntity.environment.Environment;
import chevy.model.staticEntity.environment.EnvironmentTypes;
import chevy.model.staticEntity.environment.OrientationTypes;
import chevy.utilz.Vector2;

public abstract class Traps extends Environment {
    TrapsTypes type;
    OrientationTypes orientation;

    public Traps(Vector2<Integer> initVelocity, OrientationTypes orientation, TrapsTypes type) {
        super(initVelocity, orientation, EnvironmentTypes.TRAP);
        this.type = type;
    }
}
