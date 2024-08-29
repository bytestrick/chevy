package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Arrow extends Projectile {
    private static int ADD_DAMAGE = 0;
    private final GlobalState loop = new GlobalState(State.LOOP, .2f, true);
    private final GlobalState end = new GlobalState(State.END, .5f);

    public Arrow(Vector2<Integer> initPosition, DirectionsModel direction) {
        super(initPosition, Type.ARROW, direction);

        this.maxDamage = 1 + ADD_DAMAGE;
        this.minDamage = 1 + ADD_DAMAGE;

        initStateMachine();
    }

    public static void changeAddDamage(int value) {
        ADD_DAMAGE = value;
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