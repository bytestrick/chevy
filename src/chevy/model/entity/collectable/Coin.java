package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.utils.Vector2;

import java.util.Random;

public class Coin extends Collectable {
    private int maxValue = 5;
    private int minValue = 2;
    public enum EnumState implements CommonEnumStates {
        IDLE,
        COLLECT
    }
    private final State idle = new State(EnumState.IDLE, 0.8f);
    private final State collect = new State(EnumState.COLLECT, 0.8f);


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

        idle.linkState(collect);
    }

    public synchronized State getState(CommonEnumStates commonEnumStates) {
        EnumState coinEnumState = (EnumState) commonEnumStates;
        return switch (coinEnumState) {
            case IDLE -> idle;
            case COLLECT -> collect;
        };
    }
}
