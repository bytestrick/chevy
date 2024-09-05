package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public class Sludge extends Trap {
    private final Vertex active = new Vertex(EnumState.SLUDGE_BUBBLES, 0.8f);
    private int nMoveToUnlock;

    public Sludge(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.SLUDGE);
        this.nMoveToUnlock = 1;

        initStateMachine();
    }

    public Sludge(Vector2<Integer> initVelocity, int nMoveToUnlock) {
        super(initVelocity, Type.SLUDGE);
        this.crossable = true;
        this.nMoveToUnlock = nMoveToUnlock;
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Sludge");
        stateMachine.setInitialState(active);
    }

    public void decreaseNMoveToUnlock() {
        --nMoveToUnlock;
    }

    public int getNMoveToUnlock() {
        return nMoveToUnlock;
    }

    public synchronized Vertex getState(CommonState commonEnumStates) {
        EnumState spikedFloorState = (EnumState) commonEnumStates;
        return switch (spikedFloorState) {
            case SLUDGE_BUBBLES -> active;
        };
    }

    public enum EnumState implements CommonState {
        SLUDGE_BUBBLES
    }
}
