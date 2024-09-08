package chevy.model.entity.staticEntity.environment.traps;

import chevy.utils.Vector2;

public final class Void extends Trap {
    public Void(Vector2<Integer> initialVelocity) {
        super(initialVelocity, Type.VOID);
        maxDamage = 1;
        minDamage = 1;
        safeToCross = false;
    }
}