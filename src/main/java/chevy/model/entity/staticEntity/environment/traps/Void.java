package chevy.model.entity.staticEntity.environment.traps;

import chevy.utils.Vector2;

public class Void extends Trap {
    public Void(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.VOID);
        this.maxDamage = 1;
        this.minDamage = 1;

        this.safeToCross = false;
    }
}
