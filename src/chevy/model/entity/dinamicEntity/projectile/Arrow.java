package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Arrow extends Projectile {
    public enum EnumState implements CommonEnumStates {
        LOOP,
        END
    }
    private final State loop = new State(EnumState.LOOP, 1f, true);
    private final State end = new State(EnumState.END);


    public Arrow(Vector2<Integer> initPosition, DirectionsModel direction) {
        super(initPosition, Type.ARROW, direction);

        this.maxDamage = 1;
        this.minDamage = 1;
        this.drawLayer = 2;

        initStateMachine();
    }


    private void initStateMachine() {
        stateMachine.setStateMachineName("Arrow");
        stateMachine.setInitialState(loop);

        loop.linkState(end);
    }

    public State getState(CommonEnumStates commonEnumStates) {
        EnumState arrowState = (EnumState) commonEnumStates;
        return switch (arrowState) {
            case LOOP -> loop;
            case END -> end;
        };
    }
}
