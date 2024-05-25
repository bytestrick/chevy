package chevy.model.staticEntity.environment.traps;

import chevy.model.staticEntity.environment.OrientationTypes;
import chevy.utilz.Vector2;

public class Sludge extends Traps {
    public Sludge(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, TrapsTypes.SLUDGE);
    }
}
