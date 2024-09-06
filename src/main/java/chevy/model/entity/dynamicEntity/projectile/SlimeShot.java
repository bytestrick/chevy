package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class SlimeShot extends Projectile {
    public final Vertex start = new Vertex(SlimeShot.State.START, 0.5f);
    public final Vertex loop = new Vertex(SlimeShot.State.LOOP, 1f, true);
    public final Vertex end = new Vertex(SlimeShot.State.END, 0.5f);

    public SlimeShot(Vector2<Integer> initPosition, Direction direction) {
        super(initPosition, Projectile.Type.SLIME_SHOT, direction);

        this.maxDamage = 3;
        this.minDamage = 2;

        initStateMachine();
    }

    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Slime shot");
        this.stateMachine.setInitialState(start);

        start.linkVertex(loop);
        start.linkVertex(end);
        loop.linkVertex(end);
    }

    @Override
    public Vertex getState(CommonState commonState) {
        State state = (State) commonState;
        return switch (state) {
            case START -> start;
            case LOOP -> loop;
            case END -> end;
        };
    }

    public enum State implements CommonState {
        START, LOOP, END
    }
}