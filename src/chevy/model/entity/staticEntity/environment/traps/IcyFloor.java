package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class IcyFloor extends Trap {
    public enum EnumState implements CommonState {
        ICY_FLOOR,
        ICY_FLOOR_SPARKLING
    }
    private final GlobalState icyFloor = new GlobalState(EnumState.ICY_FLOOR, 2f);
    private final GlobalState icyFloorSparkling = new GlobalState(EnumState.ICY_FLOOR_SPARKLING, 0.8f);


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


    public synchronized GlobalState getState(CommonState commonEnumStates) {
        EnumState icyFloorState = (EnumState) commonEnumStates;
        return switch (icyFloorState) {
            case ICY_FLOOR -> icyFloor;
            case ICY_FLOOR_SPARKLING -> icyFloorSparkling;
        };
    }
}
