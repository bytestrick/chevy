package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.Point;

/**
 * Punto in cui il player si deve recare per poter passare alla stanza successiva.
 * La scala rimane bloccata finché non si eliminano tutti i nemici presenti nella stanza. Dopo
 * averli eliminati tutti si apre e permette il passaggio a un altra stanza.
 */
public class Stair extends Environment {
    private final Vertex idle = new Vertex(State.IDLE);
    private final Vertex open = new Vertex(State.OPEN, .3f);
    private final Vertex idleEntry = new Vertex(State.IDLE_ENTRY, .5f);

    public Stair(Point position) {
        super(position, Type.STAIR);
        crossable = true;
        shouldUpdate = true;
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Stair");
        stateMachine.setInitialState(idle);

        idle.linkVertex(open);
        open.linkVertex(idleEntry);
    }

    @Override
    public Vertex getState(EntityState state) {
        State stairState = (State) state;
        return switch (stairState) {
            case IDLE -> idle;
            case OPEN -> open;
            case IDLE_ENTRY -> idleEntry;
        };
    }

    public enum State implements EntityState {IDLE, OPEN, IDLE_ENTRY}
}