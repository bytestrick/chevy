package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Utils;
import chevy.utils.Vector2;

public final class Coin extends Collectable {
    private final Vertex idle = new Vertex(State.IDLE, 0.8f);
    private final Vertex collected = new Vertex(State.COLLECTED, 0.8f);

    public Coin(Vector2<Integer> initPosition) {
        super(initPosition, Type.COIN);
        initStateMachine();
    }

    public int getValue() {
        int maxValue = 5;
        int minValue = 2;
        return Utils.random.nextInt(minValue, maxValue);
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Coin");
        stateMachine.setInitialState(idle);

        idle.linkVertex(collected);
    }

    public synchronized Vertex getState(CommonState commonState) {
        Coin.State coinState = (State) commonState;
        return switch (coinState) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }

    public enum State implements CommonState {
        IDLE, COLLECTED
    }
}