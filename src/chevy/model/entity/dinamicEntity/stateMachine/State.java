package chevy.model.entity.dinamicEntity.stateMachine;

import java.util.*;

public class State {
    private final EnumState enumState;
    private final boolean selfLoop; // auto arco
    private final Dictionary<EnumState, State> linkedStates = new Hashtable<>();


    public State(EnumState enumState) {
        this.enumState = enumState;
        this.selfLoop = false;
    }

    public State(EnumState enumState, boolean selfLoop) {
        this.enumState = enumState;
        this.selfLoop = selfLoop;
    }


    public EnumState getStateEnum() {
        return enumState;
    }

    public void linkState(State state) {
        linkedStates.put(state.getStateEnum(), state);
    }

    public State findState(EnumState enumState) {
        if (selfLoop && Objects.equals(this.enumState, enumState))
            return this;
        return linkedStates.get(enumState);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state1 = (State) o;

        return Objects.equals(enumState, state1.enumState);
    }

    @Override
    public int hashCode() {
        return enumState.hashCode();
    }

    @Override
    public String toString() {
        return enumState.toString();
    }
}
