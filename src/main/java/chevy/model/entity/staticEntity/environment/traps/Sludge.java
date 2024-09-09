package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.Point;

public class Sludge extends Trap {
    private final Vertex active = new Vertex(State.SLUDGE_BUBBLES, 0.8f);
    private int nMoveToUnlock;

    public Sludge(Point initVelocity) {
        super(initVelocity, Type.SLUDGE);
        nMoveToUnlock = 1;
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Sludge");
        stateMachine.setInitialState(active);
    }

    public void decreaseNMoveToUnlock() {--nMoveToUnlock;}

    public int getNMoveToUnlock() {return nMoveToUnlock;}

    public synchronized Vertex getState(EntityState state) {
        State spikedFloorState = (State) state;
        return switch (spikedFloorState) {
            case SLUDGE_BUBBLES -> active;
        };
    }

    public enum State implements EntityState {SLUDGE_BUBBLES}
}