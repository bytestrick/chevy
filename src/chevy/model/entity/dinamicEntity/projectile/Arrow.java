package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Arrow extends Projectile {
    private final GlobalState loop = new GlobalState(State.LOOP, .2f, true);
    private final GlobalState end = new GlobalState(State.END);

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

    public GlobalState getState(CommonState commonState) {
        State arrowState = (State) commonState;
        return switch (arrowState) {
            case LOOP -> loop;
            case END -> end;
        };
    }

    public enum State implements CommonState {
        LOOP, END
    }
}