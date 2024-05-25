package chevy.model.staticEntity.environment.traps;

import chevy.model.staticEntity.environment.OrientationTypes;
import chevy.utilz.Vector2;

public class Void extends Traps {
    public Void(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, TrapsTypes.VOID);
    }
}
