package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class SlimeShot extends Projectile {
    public final State start = new State(States.START, 0.5f);
    public final State loop = new State(States.LOOP, 1f, true);
    public final State end = new State(States.END, 0.5f);

    public SlimeShot(Vector2<Integer> initPosition, DirectionsModel direction, float advanceTimer) {
        super(initPosition, Projectile.Type.SLIME_SHOT, direction, advanceTimer);

        this.maxDamage = 3;
        this.minDamage = 2;
        this.layer = 2;

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
    public State getState(CommonStates commonEnumStates) {
        States projectileState = (States) commonEnumStates;
        return switch (projectileState) {
            case START -> start;
            case LOOP -> loop;
            case END -> end;
        };
    }
}