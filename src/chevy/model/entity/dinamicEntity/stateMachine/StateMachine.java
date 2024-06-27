package chevy.model.entity.dinamicEntity.stateMachine;

/**
 * La classe StateMachine rappresenta una macchina a stati finiti (FSM - Finite State Machine)
 * che gestisce le transizioni tra i diversi stati.
 * Questa classe consente di definire un comportamento strutturato in cui un entità dinamica può trovarsi
 * in uno e un solo stato alla volta, cambiando stato in risposta a determinati eventi o condizioni.
 * Prima di usare la macchina a stati finiti è necessario definire uno stato di partenza. Successivamente è
 * possibile cambiare stato attraverso il metodo checkAndChangeState(), oppure usando in modo combinato
 * il metodo canChange() e changeState(). Se si usa solo changeState() non si controlla che lo stato corrente
 * sia finito.
 */
public class StateMachine {
    private State currentState;
    private State previousState;
    private State nextState;
    private boolean usedWithCanChange = false;
    private String stateMachineName; // solo per capire di chi è la stampa


    public StateMachine() {}


    public synchronized boolean changeState(CommonEnumStates state) {
        if (currentState == null) {
            System.out.println("[x] Non è presente uno stato iniziale");
            return false;
        }

        if (stateMachineName != null)
            System.out.print(stateMachineName + ": " + currentState);

        if (!usedWithCanChange)
            nextState = currentState.findLinkedState(state);

        if (nextState != null) {
            previousState = currentState;
            currentState = nextState;

            if (stateMachineName != null) {
                System.out.println(" -> " + nextState);
            }

            currentState.startStateTimer();
            nextState = null;
            return true;
        }

        if (stateMachineName != null)
            System.out.println();

        return false;
    }

    public synchronized boolean canChange(CommonEnumStates state) {
        if (currentState == null) {
            System.out.println("[x] Non è presente uno stato iniziale");
            return false;
        }

        nextState = currentState.findLinkedState(state);
        if (nextState == null) {
            return false;
        }

        return currentState.isFinished();
    }


    public synchronized boolean checkAndChangeState(CommonEnumStates state) {
        if (canChange(state)) {
            usedWithCanChange = true;
            changeState(state);
            usedWithCanChange = false;
            return true;
        }
        return false;
    }


    public synchronized boolean changeToPreviousState() {
        return changeState(previousState.getStateEnum());
    }

    public synchronized State getCurrentState() { return currentState; }

    public synchronized State getPreviousState() { return previousState; }

    public void setInitialState(State startState) {
        this.currentState = startState;
        this.currentState.startStateTimer();
    }

    // solo per la stampa
    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }
}
