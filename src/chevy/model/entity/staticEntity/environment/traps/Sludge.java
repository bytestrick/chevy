package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Sludge extends Trap {
    private int nMoveToUnlock;
    public enum EnumState implements CommonStates {
        SLUDGE_BUBBLES
    }

    private final State active = new State(EnumState.SLUDGE_BUBBLES, 0.8f);


    public Sludge(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.SLUDGE);
        this.nMoveToUnlock = 1;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Sludge");
        stateMachine.setInitialState(active);
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

    public synchronized State getState(CommonStates commonEnumStates) {
        EnumState spikedFloorState = (EnumState) commonEnumStates;
        return switch (spikedFloorState) {
            case SLUDGE_BUBBLES -> active;
        };
    }
}
