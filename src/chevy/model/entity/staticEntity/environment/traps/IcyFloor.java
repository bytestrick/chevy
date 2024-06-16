package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class IcyFloor extends Trap {
    public IcyFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.ICY_FLOOR);
    }
}
