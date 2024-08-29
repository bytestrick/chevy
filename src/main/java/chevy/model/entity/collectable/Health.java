package chevy.model.entity.collectable;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Health extends Collectable {
    private final GlobalState idle = new GlobalState(State.IDLE, 1.6f);
    private final GlobalState collected = new GlobalState(State.COLLECTED, 0.8f);
    private int recoverHealth = 2;

    public Health(Vector2<Integer> initPosition) {
        super(initPosition, Type.HEALTH);

        initStateMachine();
    }

    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Health");
        this.stateMachine.setInitialState(idle);

        idle.linkState(collected);
    }

    public void changeRecoverHealth(int newRecoverHealth) {
        this.recoverHealth = newRecoverHealth;
    }

    public int getRecoverHealth() {
        if (isCollected()) {
            return 0;
        }

        return recoverHealth;
    }

    public synchronized GlobalState getState(CommonState commonState) {
        State healthState = (State) commonState;
        return switch (healthState) {
            case IDLE -> idle;
            case COLLECTED -> collected;
        };
    }

    public enum State implements CommonState {
        IDLE, COLLECTED
    }
}