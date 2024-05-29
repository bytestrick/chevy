package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class Trapdoor extends Traps {
    public Trapdoor(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.TRAPDOOR);
    }
}
