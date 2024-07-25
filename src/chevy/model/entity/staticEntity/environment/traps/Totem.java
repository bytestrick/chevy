package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Totem extends Trap {
    public enum EnumState implements CommonEnumStates {
        SHOT,
        RELOAD
    }
    private final DirectionsModel directionShot;
    private final State shot = new State(EnumState.SHOT, 3f);
    private final State reload = new State(EnumState.RELOAD, 10f);


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
    public State getState(CommonEnumStates commonEnumStates) {
        EnumState totemState = (EnumState) commonEnumStates;
        return switch (totemState) {
            case SHOT -> shot;
            case RELOAD -> reload;
        };
    }
}
