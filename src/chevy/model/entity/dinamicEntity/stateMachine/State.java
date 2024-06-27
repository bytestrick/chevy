package chevy.model.entity.dinamicEntity.stateMachine;

import chevy.model.Timer;

import java.util.*;

public class State {
    private final CommonEnumStates commonEnumStates;
    private final boolean selfLoop; // auto arco
    private final float durationState;
    private final Dictionary<CommonEnumStates, State> linkedStates = new Hashtable<>();
    // ---
    private final Timer stateTimer;


    public State(CommonEnumStates commonEnumStates) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = false;
        this.durationState = 0.f;
        this.stateTimer = null;
    }

    public State(CommonEnumStates commonEnumStates, float secDuration) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = false;
        this.durationState = secDuration;
        this.stateTimer = new Timer(durationState);
    }

    public State(CommonEnumStates commonEnumStates, boolean selfLoop) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = selfLoop;
        this.durationState = 0.f;
        this.stateTimer = null;
    }

    public State(CommonEnumStates commonEnumStates, float duration, boolean selfLoop) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = selfLoop;
        this.durationState = duration;
        this.stateTimer = new Timer(durationState);
    }


    public CommonEnumStates getStateEnum() {
        return commonEnumStates;
    }

    public void linkState(State state) {
        linkedStates.put(state.getStateEnum(), state);
    }

    public State findLinkedState(CommonEnumStates commonEnumStates) {
        if (selfLoop && Objects.equals(this.commonEnumStates, commonEnumStates))
            return this;
        return linkedStates.get(commonEnumStates);
    }

    public float getDuration() {
        return durationState;
    }


    public boolean isFinished() {
        if (stateTimer == null)
            return true;

       return !stateTimer.isRunning();
    }

    public void startStateTimer() {
        if (stateTimer != null && !stateTimer.isRunning()) {
            stateTimer.restart();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state1 = (State) o;

        return Objects.equals(commonEnumStates, state1.commonEnumStates);
    }

    @Override
    public int hashCode() {
        return commonEnumStates.hashCode();
    }

    @Override
    public String toString() {
        return commonEnumStates.toString();
    }
}
