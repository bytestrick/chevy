package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class Totem extends Traps {
    public Totem(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.TOTEM);
        this.crossable = false;
    }
}
