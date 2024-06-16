package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class Void extends Trap {
    public Void(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.VOID);
        this.maxDamage = 1;
        this.minDamage = 1;

        this.safeToCross = false;
    }
}
