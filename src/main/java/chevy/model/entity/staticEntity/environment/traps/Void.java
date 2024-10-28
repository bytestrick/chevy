package chevy.model.entity.staticEntity.environment.traps;

import java.awt.Point;

public final class Void extends Trap {
    public Void(Point initialVelocity) {
        super(initialVelocity, Type.VOID);
        maxDamage = 1;
        minDamage = 1;
        safeToCross = false;
    }
}
