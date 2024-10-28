package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.Point;

public class IcyFloor extends Trap {
    private final Vertex icyFloor = new Vertex(State.ICY_FLOOR, 2f);
    private final Vertex icyFloorSparkling = new Vertex(State.ICY_FLOOR_SPARKLING, 0.8f);

    public IcyFloor(Point position) {
        super(position, Type.ICY_FLOOR);
        shouldUpdate = true;
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Icy floor");
        stateMachine.setInitialState(icyFloor);

        icyFloor.linkVertex(icyFloorSparkling);
        icyFloorSparkling.linkVertex(icyFloor);
    }

    public synchronized Vertex getState(EntityState state) {
        State icyFloorState = (State) state;
        return switch (icyFloorState) {
            case ICY_FLOOR -> icyFloor;
            case ICY_FLOOR_SPARKLING -> icyFloorSparkling;
        };
    }

    public enum State implements EntityState {ICY_FLOOR, ICY_FLOOR_SPARKLING}
}
