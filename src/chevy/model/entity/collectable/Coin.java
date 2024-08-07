package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

import java.util.Random;

public class Coin extends Collectable {
    private final GlobalState idle = new GlobalState(State.IDLE, 0.8f);
    private final GlobalState collected = new GlobalState(State.COLLECTED, 0.8f);
    private int maxValue = 5;
    private int minValue = 2;

    public Coin(Vector2<Integer> initPosition) {
        super(initPosition, Type.COIN);

        initStateMachine();
    }

    private int getValue() {
        return new Random().nextInt(minValue, maxValue);
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Coin");
        stateMachine.setInitialState(idle);

        idle.linkState(collected);
    }

    public synchronized GlobalState getState(CommonState commonState) {
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