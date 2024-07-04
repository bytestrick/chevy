package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class IcyFloor extends Trap {
    public enum EnumState implements CommonStates {
        ICY_FLOOR,
        ICY_FLOOR_SPARKLING
    }
    private final State icyFloor = new State(EnumState.ICY_FLOOR, 2f);
    private final State icyFloorSparkling = new State(EnumState.ICY_FLOOR_SPARKLING, 0.8f);


    public IcyFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.ICY_FLOOR);
        this.mustBeUpdated = true;

        initStateMachine();
    }


    private void initStateMachine() {
//        stateMachine.setStateMachineName("Icy floor");
        stateMachine.setInitialState(icyFloor);

        icyFloor.linkState(icyFloorSparkling);
        icyFloorSparkling.linkState(icyFloor);
    }


    public synchronized State getState(CommonStates commonEnumStates) {
        EnumState icyFloorState = (EnumState) commonEnumStates;
        return switch (icyFloorState) {
            case ICY_FLOOR -> icyFloor;
            case ICY_FLOOR_SPARKLING -> icyFloorSparkling;
        };
    }
}
