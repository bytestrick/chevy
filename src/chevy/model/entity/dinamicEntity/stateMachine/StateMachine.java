package chevy.model.entity.dinamicEntity.stateMachine;

public class StateMachine {
    private State currentState;

    public StateMachine() {}

    public boolean changeState(StateEnum state) {
        if (currentState == null) {
            System.out.println("Non è presente uno stato iniziale");
            return false;
        }

        System.out.print(currentState.toString());

        State nextState = currentState.findState(state);
        if (nextState != null) {
            currentState = nextState;

            System.out.println(" -> " + nextState);

            return true;
        }

        System.out.println();

        return false;
    }

    public State getCurrentState() { return currentState; }

    public void setInitialState(State startState) { this.currentState = startState; }
}
