package chevy.model.staticEntity.environment;

import chevy.model.staticEntity.Entity;
import chevy.utilz.Vector2;

public abstract class Environment extends Entity {
    EnvironmentTypes type;
    OrientationTypes orientation;

    public Environment(Vector2<Integer> initVelocity, OrientationTypes orientation, EnvironmentTypes type) {
        super(initVelocity);
        this.type = type;
    }
}
