package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Key extends Collectable {
    private final Vertex idle = new Vertex(State.IDLE, 1.8f);
    private final Vertex collected = new Vertex(State.COLLECTED, 0.8f);

    public Key(Vector2<Integer> initPosition) {
        super(initPosition, Type.KEY);

        initStateMachine();
    }

    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Key");
        this.stateMachine.setInitialState(idle);

        idle.linkVertex(collected);
    }

    public synchronized Vertex getState(CommonState commonState) {
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