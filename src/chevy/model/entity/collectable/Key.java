package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Key extends Collectable {
    public enum EnumState implements CommonEnumStates {
        IDLE,
        COLLECTED
    }

    private final State idle = new State(EnumState.IDLE, 1.8f);
    private final State collected = new State(EnumState.COLLECTED, 0.8f);


    public Key(Vector2<Integer> initPosition) {
        super(initPosition, Type.KEY);

        initStateMachine();
    }


    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Key");
        this.stateMachine.setInitialState(idle);

        idle.linkState(collected);
    }

    public synchronized State getState(CommonEnumStates commonEnumStates) {
        EnumState keyEnumState = (EnumState) commonEnumStates;
        return switch (keyEnumState) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }
}
