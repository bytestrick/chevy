package chevy.model.staticEntity.traps;

import chevy.utilz.Vector2;

public class SpikedFloor extends Traps {
    public SpikedFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.SPIKED_FLOOR);
    }
}
