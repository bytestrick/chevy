package chevy.model.staticEntity.environment;

import chevy.utilz.Vector2;

public class Wall extends Environment {
    public Wall(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, EnvironmentTypes.WALL);
    }
}
