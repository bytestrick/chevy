package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class SpikedFloor extends Traps {
    private boolean active;


    public SpikedFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, TrapsTypes.SPIKED_FLOOR);
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
