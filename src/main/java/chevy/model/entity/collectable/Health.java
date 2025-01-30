package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.*;

public final class Health extends Collectable {
    private final Vertex idle = new Vertex(State.IDLE, 1.6f);
    private final Vertex collected = new Vertex(State.COLLECTED, 0.8f);

    public Health(Point position) {
        super(position, Type.HEALTH);
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Health");
        stateMachine.setInitialState(idle);

        idle.linkVertex(collected);
    }

    public int getRecoverHealth() {
        return isCollected() ? 0 : 2;
    }

    public synchronized Vertex getState(EntityState state) {
        return switch ((State) state) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }
}
