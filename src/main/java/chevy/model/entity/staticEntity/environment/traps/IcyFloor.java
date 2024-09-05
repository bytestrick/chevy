package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public class IcyFloor extends Trap {
    private final Vertex icyFloor = new Vertex(EnumState.ICY_FLOOR, 2f);
    private final Vertex icyFloorSparkling = new Vertex(EnumState.ICY_FLOOR_SPARKLING, 0.8f);

    public IcyFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.ICY_FLOOR);
        this.mustBeUpdate = true;

        initStateMachine();
    }

    private void initStateMachine() {
//        stateMachine.setStateMachineName("Icy floor");
        stateMachine.setInitialState(icyFloor);

        icyFloor.linkState(icyFloorSparkling);
        icyFloorSparkling.linkState(icyFloor);
    }

    public synchronized Vertex getState(CommonState commonEnumStates) {
        EnumState icyFloorState = (EnumState) commonEnumStates;
        return switch (icyFloorState) {
            case ICY_FLOOR -> icyFloor;
            case ICY_FLOOR_SPARKLING -> icyFloorSparkling;
        };
    }

    public enum EnumState implements CommonState {
        ICY_FLOOR, ICY_FLOOR_SPARKLING
    }
}
