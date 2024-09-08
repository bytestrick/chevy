package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public class SpikedFloor extends Trap {
    private final Vertex activated = new Vertex(State.ACTIVATED, .2f);
    private final Vertex disabled = new Vertex(State.DISABLED, 3f);
    private final Vertex damage = new Vertex(State.DAMAGE, .8f);

    public SpikedFloor(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.SPIKED_FLOOR);
        safeToCross = true;
        shouldUpdate = true;
        maxDamage = 2;
        minDamage = 1;
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Spiked floor");
        stateMachine.setInitialState(disabled);

        disabled.linkVertex(activated);
        activated.linkVertex(damage);
        damage.linkVertex(disabled);
    }

    public synchronized Vertex getState(EntityState state) {
        return switch ((State) state) {
            case ACTIVATED -> activated;
            case DISABLED -> disabled;
            case DAMAGE -> damage;
        };
    }

    public enum State implements EntityState {ACTIVATED, DAMAGE, DISABLED}
}