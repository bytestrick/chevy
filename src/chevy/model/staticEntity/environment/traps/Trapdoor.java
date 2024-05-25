package chevy.model.staticEntity.environment.traps;

import chevy.model.staticEntity.environment.OrientationTypes;
import chevy.utilz.Vector2;

public class Trapdoor extends Traps {
    public Trapdoor(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, TrapsTypes.TRAPDOOR);
    }
}
