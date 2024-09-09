package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Arrow extends Projectile {
    private static int addDamage;
    private final Vertex loop = new Vertex(State.LOOP, .2f, true);
    private final Vertex end = new Vertex(State.END, .5f);

    public Arrow(Vector2<Integer> initPosition, Direction direction) {
        super(initPosition, Type.ARROW, direction);
        maxDamage = 1 + addDamage;
        minDamage = 1 + addDamage;
        initStateMachine();
    }

    public static void changeAddDamage(int value) {addDamage = value;}

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