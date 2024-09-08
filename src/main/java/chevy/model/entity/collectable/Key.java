package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Key extends Collectable {
    private final Vertex idle = new Vertex(State.IDLE, 1.8f);
    private final Vertex collected = new Vertex(State.COLLECTED, .8f);

    public Key(Vector2<Integer> initPosition) {
        super(initPosition, Type.KEY);

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Key");
        stateMachine.setInitialState(idle);

        idle.linkVertex(collected);
    }

    public synchronized Vertex getState(EntityState state) {
        return switch ((State) state) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }
}