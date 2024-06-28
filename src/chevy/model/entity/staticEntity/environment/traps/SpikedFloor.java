package chevy.model.entity.staticEntity.environment.traps;

import chevy.utils.Vector2;

public class SpikedFloor extends Trap {
    private boolean active;


    public SpikedFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.SPIKED_FLOOR);
        this.active = false;
        this.safeToCross = true;
        this.maxDamage = 2;
        this.minDamage = 1;
    }


    public void toggleStateActive() {
        active = !active;
        this.safeToCross = !active;
    }

    public boolean isActive() {
        return active;
    }
}
