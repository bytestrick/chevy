package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Health extends Collectable {
    public enum EnumState implements CommonEnumStates {
        IDLE,
        COLLECT
    }
    private final State idle = new State(EnumState.IDLE, 1.6f);
    private final State collect = new State(EnumState.COLLECT, 0.8f);
    private int recoverHealth = 2;


    public Health(Vector2<Integer> initPosition) {
        super(initPosition, Type.HEALTH);

        initStateMachine();
    }


    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Health");
        this.stateMachine.setInitialState(idle);

        idle.linkState(collect);
    }

    public void changeRecoverHealth(int newRecoverHealth) {
        this.recoverHealth = newRecoverHealth;
    }

    public int getRecoverHealth() {
        if (isCollected())
            return 0;

        return recoverHealth;
    }

    public synchronized State getState(CommonEnumStates commonEnumStates) {
        EnumState healthEnumState = (EnumState) commonEnumStates;
        return switch (healthEnumState) {
            case IDLE -> idle;
            case COLLECT -> collect;
        };
    }
}
