package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class SpikedFloor extends Traps {
    public SpikedFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.SPIKED_FLOOR);
    }
}
