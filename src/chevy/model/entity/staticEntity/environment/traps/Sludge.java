package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class Sludge extends Traps {
    public Sludge(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.SLUDGE);
    }
}
