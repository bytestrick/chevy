package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public class Trapdoor extends Trap {
    private final Vertex idle = new Vertex(EnumState.IDLE);
    private final Vertex open = new Vertex(EnumState.OPEN, 0.4f);
    private final Vertex damage = new Vertex(EnumState.DAMAGE);

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

    public synchronized Vertex getState(CommonState commonEnumStates) {
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