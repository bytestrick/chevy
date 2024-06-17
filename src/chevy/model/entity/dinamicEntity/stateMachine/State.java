package chevy.model.entity.dinamicEntity.stateMachine;

import chevy.model.Timer;

import java.util.*;

public class State {
    private final EnumState enumState;
    private final boolean selfLoop; // auto arco
    private final float durationState;
    private final Dictionary<EnumState, State> linkedStates = new Hashtable<>();
    // ---
    private final Timer stateTimer;


    public State(EnumState enumState) {
        this.enumState = enumState;
        this.selfLoop = false;
        this.durationState = 0;
        this.stateTimer = null;
    }

    public State(EnumState enumState, float secDuration) {
        this.enumState = enumState;
        this.selfLoop = false;
        this.durationState = secDuration;
        this.stateTimer = new Timer(durationState);
    }

    public State(EnumState enumState, boolean selfLoop) {
        this.enumState = enumState;
        this.selfLoop = selfLoop;
        this.durationState = 0;
        this.stateTimer = null;
    }

    public State(EnumState enumState, float duration, boolean selfLoop) {
        this.enumState = enumState;
        this.selfLoop = selfLoop;
        this.durationState = duration;
        this.stateTimer = new Timer(durationState);
    }


    public EnumState getStateEnum() {
        return enumState;
    }

    public void linkState(State state) {
        linkedStates.put(state.getStateEnum(), state);
    }

    public State findLinkedState(EnumState enumState) {
        if (selfLoop && Objects.equals(this.enumState, enumState))
            return this;
        return linkedStates.get(enumState);
    }

    public float getDuration() {
        return durationState;
    }


    public boolean isFinished() {
        if (stateTimer == null)
            return true;

       return stateTimer.isEnd();
    }

    public void startStateTimer() {
        if (stateTimer != null && !stateTimer.isStart())
            stateTimer.start();
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
