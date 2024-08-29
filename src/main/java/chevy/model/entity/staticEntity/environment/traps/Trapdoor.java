package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Trapdoor extends Trap {
    private final GlobalState idle = new GlobalState(EnumState.IDLE);
    private final GlobalState open = new GlobalState(EnumState.OPEN, 0.4f);
    private final GlobalState damage = new GlobalState(EnumState.DAMAGE);

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

    public synchronized GlobalState getState(CommonState commonEnumStates) {
        EnumState trapdoorState = (EnumState) commonEnumStates;
        return switch (trapdoorState) {
            case IDLE -> idle;
            case OPEN -> open;
            case DAMAGE -> damage;
        };
    }

    public enum EnumState implements CommonState {
        IDLE, OPEN, DAMAGE
    }
}