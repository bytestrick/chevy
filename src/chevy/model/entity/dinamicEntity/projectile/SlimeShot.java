package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class SlimeShot extends Projectile {
    public final GlobalState start = new GlobalState(SlimeShot.State.START, 0.5f);
    public final GlobalState loop = new GlobalState(SlimeShot.State.LOOP, 1f, true);
    public final GlobalState end = new GlobalState(SlimeShot.State.END, 0.5f);
    public SlimeShot(Vector2<Integer> initPosition, DirectionsModel direction, float advanceTimer) {
        super(initPosition, Projectile.Type.SLIME_SHOT, direction);

        this.maxDamage = 3;
        this.minDamage = 2;
        this.drawLayer = 2;

        initStateMachine();
    }

    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Slime shot");
        this.stateMachine.setInitialState(start);

        start.linkState(loop);
        start.linkState(end);
        loop.linkState(end);
    }

    @Override
    public GlobalState getState(CommonState commonState) {
        State state = (State) commonState;
        return switch (state) {
            case START -> start;
            case LOOP -> loop;
            case END -> end;
        };
    }

    public enum State implements CommonState {
        START, LOOP, END;
    }
}