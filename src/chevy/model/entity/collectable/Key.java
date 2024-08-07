package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Key extends Collectable {
    private final GlobalState idle = new GlobalState(State.IDLE, 1.8f);
    private final GlobalState collected = new GlobalState(State.COLLECTED, 0.8f);
    public Key(Vector2<Integer> initPosition) {
        super(initPosition, Type.KEY);

        initStateMachine();
    }

    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Key");
        this.stateMachine.setInitialState(idle);

        idle.linkState(collected);
    }

    public synchronized GlobalState getState(CommonState commonState) {
        State keyState = (State) commonState;
        return switch (keyState) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }

    public enum State implements CommonState {
        IDLE, COLLECTED
    }
}