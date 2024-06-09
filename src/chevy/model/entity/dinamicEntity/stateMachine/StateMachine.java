package chevy.model.entity.dinamicEntity.stateMachine;

public class StateMachine {
    private State currentState;
    private State previousState;
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
            previousState = currentState;
            currentState = nextState;

            System.out.println(" -> " + nextState);

            return true;
        }

        System.out.println();

        return false;
    }

    public synchronized boolean changeToPreviousState() {
        return changeState(previousState.getStateEnum());
    }

    public synchronized State getCurrentState() { return currentState; }

    public synchronized State getPreviousState() { return previousState; }

    public void setInitialState(State startState) { this.currentState = startState; }

    // solo per la stampa
    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }
}
