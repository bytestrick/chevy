package chevy.model.entity.dinamicEntity.stateMachine;

import java.util.*;

public class State {
    private final String state;
    private final Dictionary<String, State> linkedStates = new Hashtable<>();

    public State(String state) {
        this.state = state;
    }

    public String getName() {
        return state;
    }

    public void linkState(State state) {
        linkedStates.put(state.getName(), state);
    }

    public State findState(String stateName) {
        return linkedStates.get(stateName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state1 = (State) o;

        return Objects.equals(state, state1.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }
}
