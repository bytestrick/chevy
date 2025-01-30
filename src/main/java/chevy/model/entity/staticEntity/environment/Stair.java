package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.*;

/**
 * Point where the player has to go to pass to the next room.
 * The stair remains blocked until all the enemies in the room are eliminated. After eliminating
 * all of them it opens and allows passage to another room.
 */
public class Stair extends Environment {
    private final Vertex idle = new Vertex(State.IDLE);
    private final Vertex open = new Vertex(State.OPEN, .3f);
    private final Vertex idleEntry = new Vertex(State.IDLE_ENTRY, .5f);

    public Stair(Point position) {
        super(position, Type.STAIR);
        crossable = true;
        shouldUpdate = true;
        drawLayer = 2;
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
