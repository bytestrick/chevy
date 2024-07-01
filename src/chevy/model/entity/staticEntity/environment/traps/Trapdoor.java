package chevy.model.entity.staticEntity.environment.traps;

import chevy.utils.Vector2;

public class Trapdoor extends Trap {
    public Trapdoor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.TRAPDOOR);
    }
}
