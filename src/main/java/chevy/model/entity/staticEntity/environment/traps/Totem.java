package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public class Totem extends Trap {
    private final Direction directionShot;
    private final Vertex shot = new Vertex(EnumState.SHOT, 3f);
    private final Vertex reload = new Vertex(EnumState.RELOAD, 5f);

    public Totem(Vector2<Integer> initVelocity, Direction directionShot) {
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

    public Direction getDirectionShot() {
        return directionShot;
    }

    @Override
    public Vertex getState(CommonState commonStates) {
        EnumState totemState = (EnumState) commonStates;
        return switch (totemState) {
            case SHOT -> shot;
            case RELOAD -> reload;
        };
    }

    public enum EnumState implements CommonState {
        SHOT, RELOAD
    }
}