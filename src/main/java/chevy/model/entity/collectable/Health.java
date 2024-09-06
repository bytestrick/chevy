package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Health extends Collectable {
    private final Vertex idle = new Vertex(State.IDLE, 1.6f);
    private final Vertex collected = new Vertex(State.COLLECTED, 0.8f);

    public Health(Vector2<Integer> initPosition) {
        super(initPosition, Type.HEALTH);

        initStateMachine();
    }

    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Health");
        this.stateMachine.setInitialState(idle);

        idle.linkVertex(collected);
    }

    public int getRecoverHealth() {
        if (isCollected()) {
            return 0;
        }

        return 2;
    }

    public synchronized Vertex getState(CommonState commonState) {
        State healthState = (State) commonState;
        return switch (healthState) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }

    public enum State implements CommonState { IDLE, COLLECTED }
}