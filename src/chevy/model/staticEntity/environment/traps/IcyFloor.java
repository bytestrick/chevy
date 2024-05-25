package chevy.model.staticEntity.environment.traps;

import chevy.model.staticEntity.environment.OrientationTypes;
import chevy.utilz.Vector2;

public class IcyFloor extends Traps {
    public IcyFloor(Vector2<Integer> initVelocity, OrientationTypes orientation) {
        super(initVelocity, orientation, TrapsTypes.ICY_FLOOR);
    }
}
