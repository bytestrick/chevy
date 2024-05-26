package chevy.model.stateMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State {
    private final String state;
    private List<State> connectedStates;

    public State(String state) {
        this.state = state;
    }

    public String getName() {
        return state;
    }

    public void linkState(State state) {
        if (connectedStates == null)
            connectedStates = new ArrayList<>();
        connectedStates.add(state);
    }

    public State findState(State state) {
        for (State s : connectedStates)
            if (s.equals(state))
                return s;
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state1 = (State) o;

        return Objects.equals(state, state1.state);
    }
}
