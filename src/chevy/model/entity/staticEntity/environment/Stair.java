package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

/**
 * Classe che rappresenta il punto in cui il player si deve recare per poter passare alla stanza successiva.
 * La scala rimane bloccata finché non si eliminano tutti i nemici presenti nella stanza, dopo averli eliminati
 * tutti si apre e permette il passaggio a un altra stanza.
 */
public class Stair extends Environment {
    public enum State implements CommonState {
        IDLE,
        OPEN,
        IDLE_ENTRY
    }
    private final GlobalState idle = new GlobalState(State.IDLE);
    private final GlobalState open = new GlobalState(State.OPEN, 0.3f);
    private final GlobalState idleEntry = new GlobalState(State.IDLE_ENTRY, 0.5f);
    private final DirectionsModel directions;

    public Stair(Vector2<Integer> initVelocity, DirectionsModel directions) {
        super(initVelocity, Type.STAIR);

        this.directions = directions;
        this.crossable = true;
        this.mustBeUpdate = true;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Stair");
        stateMachine.setInitialState(idle);

        idle.linkState(open);
        open.linkState(idleEntry);
    }

    public DirectionsModel getDirections() {
        return directions;
    }

    @Override
    public GlobalState getState(CommonState commonEnumStates) {
        State stairState = (State) commonEnumStates;
        return switch (stairState) {
            case IDLE -> idle;
            case OPEN -> open;
            case IDLE_ENTRY -> idleEntry;
        };
    }
}
