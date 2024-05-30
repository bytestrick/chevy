package chevy.model.entity.dinamicEntity.stateMachine;

import java.util.*;

public class State {
    private final StateEnum stateEnum;
    private final boolean selfLoop; // auto arco
    private final Dictionary<StateEnum, State> linkedStates = new Hashtable<>();


    public State(StateEnum stateEnum) {
        this.stateEnum = stateEnum;
        this.selfLoop = false;
    }

    public State(StateEnum stateEnum, boolean selfLoop) {
        this.stateEnum = stateEnum;
        this.selfLoop = selfLoop;
    }


    public StateEnum getStateEnum() {
        return stateEnum;
    }

    public void linkState(State state) {
        linkedStates.put(state.getStateEnum(), state);
    }

    public State findState(StateEnum stateEnum) {
        if (selfLoop && Objects.equals(this.stateEnum, stateEnum))
            return this;
        return linkedStates.get(stateEnum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state1 = (State) o;

        return Objects.equals(stateEnum, state1.stateEnum);
    }

    @Override
    public int hashCode() {
        return stateEnum.hashCode();
    }

    @Override
    public String toString() {
        return stateEnum.toString();
    }
}
