package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Utils;

import java.awt.*;

public final class Coin extends Collectable {
    private final Vertex idle = new Vertex(State.IDLE, .8f);
    private final Vertex collected = new Vertex(State.COLLECTED, .8f);

    public Coin(Point position) {
        super(position, Type.COIN);
        initStateMachine();
    }

    public static int getValue() {
        final int minValue = 2, maxValue = 5;
        return Utils.random.nextInt(minValue, maxValue);
    }

    private void initStateMachine() {
        stateMachine.setName("Coin");
        stateMachine.setInitialState(idle);

        idle.linkVertex(collected);
    }

    public synchronized Vertex getState(EntityState state) {
        return switch ((State) state) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }
}
