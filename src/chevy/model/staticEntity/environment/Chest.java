package chevy.model.staticEntity.environment;

import chevy.utilz.Vector2;

public class Chest extends Environment {
    public Chest(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, EnvironmentTypes.CHEST);
    }
}
