package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class SpikedFloor extends Trap {
    public enum EnumState implements CommonEnumStates {
        ACTIVATED,
        DAMAGE,
        DISABLED
    }
    private final State activated = new State(EnumState.ACTIVATED, 0.2f);
    private final State disabled = new State(EnumState.DISABLED, 3f);
    private final State damage = new State(EnumState.DAMAGE, 0.8f);


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

    public synchronized State getState(CommonEnumStates commonEnumStates) {
        EnumState spikedFloorState = (EnumState) commonEnumStates;
        return switch (spikedFloorState) {
            case ACTIVATED -> activated;
            case DISABLED -> disabled;
            case DAMAGE -> damage;
        };
    }
}
