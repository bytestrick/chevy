package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;

import java.awt.Point;

public class Totem extends Trap {
    private final Direction shotDirection;
    private final Vertex shot = new Vertex(State.SHOT, 3f);
    private final Vertex reload = new Vertex(State.RELOAD, 5f);

    public Totem(Point position, Direction shotDirection) {
        super(position, Type.TOTEM);
        this.shotDirection = shotDirection;
        crossable = false;
        shouldUpdate = true;
        drawLayer = 3;
        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setName("Totem");
        stateMachine.setInitialState(reload);

        reload.linkVertex(shot);
        shot.linkVertex(reload);
    }

    @Override
    public Direction getShotDirection() {return shotDirection;}

    @Override
    public Vertex getState(EntityState state) {
        return switch ((State) state) {
            case SHOT -> shot;
            case RELOAD -> reload;
        };
    }

    public enum State implements EntityState {SHOT, RELOAD}
}