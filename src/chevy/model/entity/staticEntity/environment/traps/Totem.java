package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Totem extends Trap {
    public enum EnumState implements CommonState {
        SHOT,
        RELOAD
    }
    private final DirectionsModel directionShot;
    private final GlobalState shot = new GlobalState(EnumState.SHOT, 3f);
    private final GlobalState reload = new GlobalState(EnumState.RELOAD, 10f);


    public Totem(Vector2<Integer> initVelocity, DirectionsModel directionShot) {
        super(initVelocity, Type.TOTEM);
        this.directionShot = directionShot;
        this.crossable = false;
        this.mustBeUpdate = true;
        this.drawLayer = 3;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Totem");
        stateMachine.setInitialState(reload);

        reload.linkState(shot);
        shot.linkState(reload);
    }


    public DirectionsModel getDirectionShot() {
        return directionShot;
    }

    @Override
    public GlobalState getState(CommonState commonStates) {
        EnumState totemState = (EnumState) commonStates;
        return switch (totemState) {
            case SHOT -> shot;
            case RELOAD -> reload;
        };
    }
}
