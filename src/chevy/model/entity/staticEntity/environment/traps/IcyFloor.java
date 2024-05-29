package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class IcyFloor extends Traps {
    public IcyFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.ICY_FLOOR);
    }
}
