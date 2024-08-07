package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class SpikedFloor extends Trap {
    private final GlobalState activated = new GlobalState(State.ACTIVATED, 0.2f);
    private final GlobalState disabled = new GlobalState(State.DISABLED, 3f);
    private final GlobalState damage = new GlobalState(State.DAMAGE, 0.8f);

    public SpikedFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.SPIKED_FLOOR);
        this.safeToCross = true;
        this.mustBeUpdate = true;

        this.maxDamage = 2;
        this.minDamage = 1;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Spiked floor");
        stateMachine.setInitialState(disabled);

        disabled.linkState(activated);
        activated.linkState(damage);
        damage.linkState(disabled);
    }

    public void activated() {
        safeToCross = false;
    }

    public void disabled() {
        safeToCross = true;
    }

    public synchronized GlobalState getState(CommonState commonState) {
        State spikedFloorState = (State) commonState;
        return switch (spikedFloorState) {
            case ACTIVATED -> activated;
            case DISABLED -> disabled;
            case DAMAGE -> damage;
        };
    }

    public enum State implements CommonState {
        ACTIVATED, DAMAGE, DISABLED
    }
}
