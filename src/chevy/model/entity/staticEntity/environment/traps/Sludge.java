package chevy.model.entity.staticEntity.environment.traps;

import chevy.utilz.Vector2;

public class Sludge extends Trap {
    private int nMoveToUnlock;


    public Sludge(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.SLUDGE);
        this.nMoveToUnlock = 1;
    }

    public Sludge(Vector2<Integer> initVelocity, int nMoveToUnlock) {
        super(initVelocity, Type.SLUDGE);
        this.crossable = true;
        this.nMoveToUnlock = nMoveToUnlock;
    }


    public void decreaseNMoveToUnlock() {
        --nMoveToUnlock;
    }

    public int getNMoveToUnlock() {
        return nMoveToUnlock;
    }
}
