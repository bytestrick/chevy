package chevy.model.staticEntity.traps;

import chevy.model.staticEntity.Entity;
import chevy.utilz.Vector2;

public abstract class Traps extends Entity {
    TrapsTypes type;

    public Traps(Vector2<Integer> initVelocity, TrapsTypes type) {
        super(initVelocity);
        this.type = type;
    }
}
