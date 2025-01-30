package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.*;

public final class SlimeShot extends Projectile {
    private final Vertex start = new Vertex(SlimeShot.State.START, .5f);
    private final Vertex loop = new Vertex(SlimeShot.State.LOOP, 1f, true);
    private final Vertex end = new Vertex(SlimeShot.State.END, .5f);

    public SlimeShot(Point position, Direction direction) {
        super(position, Projectile.Type.SLIME_SHOT, direction);
        maxDamage = 3;
        minDamage = 2;
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Slime shot");
        stateMachine.setInitialState(start);

        start.linkVertex(loop);
        start.linkVertex(end);
        loop.linkVertex(end);
    }

    @Override
    public Vertex getState(EntityState state) {
        return switch ((State) state) {
            case LOOP -> loop;
            case END -> end;
            case START -> start;
        };
    }
}
