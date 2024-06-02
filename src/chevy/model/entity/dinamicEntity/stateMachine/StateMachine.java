package chevy.model.entity.dinamicEntity.stateMachine;

public class StateMachine {
    private State currentState;
    private String stateMachineName; // solo per capire di chi è la stampa


    public StateMachine() {}


    public synchronized boolean changeState(EnumState state) {
        if (currentState == null) {
            System.out.println("Non è presente uno stato iniziale");
            return false;
        }

        System.out.print(stateMachineName + ": " + currentState);

        State nextState = currentState.findState(state);
        if (nextState != null) {
            currentState = nextState;

            System.out.println(" -> " + nextState);

            return true;
        }

        System.out.println();

        return false;
    }

    public synchronized State getCurrentState() { return currentState; }

    public void setInitialState(State startState) { this.currentState = startState; }

    // solo per la stampa
    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }
}
