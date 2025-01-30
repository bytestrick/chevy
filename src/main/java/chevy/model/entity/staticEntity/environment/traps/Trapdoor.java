package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.*;

public class Trapdoor extends Trap {
    private final Vertex idle = new Vertex(State.IDLE);
    private final Vertex open = new Vertex(State.OPEN, .4f);
    private final Vertex damage = new Vertex(State.DAMAGE);

    public Trapdoor(Point position) {
        super(position, Type.TRAPDOOR);
        maxDamage = 1;
        minDamage = 1;
        shouldUpdate = true;
        safeToCross = true;
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Trapdoor");
        stateMachine.setInitialState(idle);

        idle.linkVertex(open);
        open.linkVertex(damage);
    }

    public synchronized Vertex getState(EntityState state) {
        State trapdoorState = (State) state;
        return switch (trapdoorState) {
            case IDLE -> idle;
            case OPEN -> open;
            case DAMAGE -> damage;
        };
    }

    public enum State implements EntityState {IDLE, OPEN, DAMAGE}
}
