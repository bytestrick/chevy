package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.Point;

public final class Arrow extends Projectile {
    private static int damageBoost;
    private final Vertex loop = new Vertex(State.LOOP, .2f, true);
    private final Vertex end = new Vertex(State.END, .5f);

    public Arrow(Point position, Direction direction) {
        super(position, Type.ARROW, direction);
        maxDamage = 1 + damageBoost;
        minDamage = 1 + damageBoost;
        initStateMachine();
    }

    public static void setDamageBoost(int damageBoost) {Arrow.damageBoost = damageBoost;}

    private void initStateMachine() {
        stateMachine.setName("Arrow");
        stateMachine.setInitialState(loop);

        loop.linkVertex(end);
    }

    @Override
    public Vertex getState(EntityState state) {
        return switch ((State) state) {
            case LOOP -> loop;
            case END -> end;
            case START -> null;
        };
    }
}