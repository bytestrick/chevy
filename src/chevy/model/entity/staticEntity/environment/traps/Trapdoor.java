package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Trapdoor extends Trap {
    public enum EnumState implements CommonEnumStates {
        IDLE,
        OPEN,
        DAMAGE
    }
    private final State idle = new State(EnumState.IDLE);
    private final State open = new State(EnumState.OPEN, 0.4f);
    private final State damage = new State(EnumState.DAMAGE);


    public Trapdoor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.TRAPDOOR);

        this.maxDamage = 1;
        this.minDamage = 1;

        this.mustBeUpdate = true;
        this.safeToCross = true;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Trapdoor");
        stateMachine.setInitialState(idle);

        idle.linkState(open);
        open.linkState(damage);
    }

    public void activated() {
        safeToCross = false;
    }

    public synchronized State getState(CommonEnumStates commonEnumStates) {
        EnumState trapdoorState = (EnumState) commonEnumStates;
        return switch (trapdoorState) {
            case IDLE -> idle;
            case OPEN -> open;
            case DAMAGE -> damage;
        };
    }
}
