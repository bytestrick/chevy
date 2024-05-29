package chevy.model.entity.dinamicEntity.stateMachine;

public class StateMachine {
    private State currentState;

    public StateMachine() {}

    public boolean changeState(State state) {
        if (currentState == null) {
            System.out.println("Non è presente uno stato iniziale");
            return false;
        }

        State nextState = currentState.findState(state.getName());
        if (nextState != null)
            currentState = nextState;

        return currentState != null;
    }

    public void setInitialState(State startState) { this.currentState = startState; }
}
