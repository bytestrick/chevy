package chevy.model.entity.stateMachine;

import chevy.utils.Log;

/**
 * La StateMachine è una macchina a stati finiti (FSM - Finite State Machine) che gestisce le transizioni tra i
 * diversi stati.
 */
public class StateMachine {
    private GlobalState currentGlobalState;
    private GlobalState previousGlobalState;
    private GlobalState nextGlobalState;
    private boolean usedWithCanChange = false;
    private String stateMachineName; // solo per capire di chi è la stampa

    public StateMachine() { }

    /**
     * Cambia lo stato corrente della macchina a stati.
     *
     * @param state L'enumerazione dello stato a cui passare.
     * @return true se lo stato è cambiato con successo, false altrimenti.
     */
    public synchronized boolean changeState(CommonState state) {
        if (currentGlobalState == null) {
            Log.error("Non è presente uno stato iniziale");
            return false;
        }

        // solo per la stampa
        String logMessage = "";
        if (stateMachineName != null) {
            logMessage += stateMachineName + ": " + currentGlobalState;
        }

        if (!usedWithCanChange) {
            nextGlobalState = currentGlobalState.findLinkedState(state);
        }

        if (nextGlobalState != null) {
            previousGlobalState = currentGlobalState;
            currentGlobalState = nextGlobalState;

            // solo per la stampa
            if (stateMachineName != null) {
                Log.info(logMessage + " -> " + nextGlobalState);
            }

            previousGlobalState.stopStateTimer();
            currentGlobalState.startStateTimer();
            nextGlobalState = null;
            return true;
        }

        // solo per la stampa
        if (stateMachineName != null) {
            Log.info(logMessage);
        }

        return false;
    }

    /**
     * Controlla se è possibile cambiare lo stato corrente della macchina a stati.
     *
     * @param state L'enumerazione dello stato a cui passare.
     * @return true se è possibile cambiare lo stato, false altrimenti.
     */
    public synchronized boolean canChange(CommonState state) {
        if (currentGlobalState == null) {
            Log.error("Non è presente uno stato iniziale");
            return false;
        }

        nextGlobalState = currentGlobalState.findLinkedState(state);
        if (nextGlobalState == null) {
            return false;
        }

        return currentGlobalState.isFinished();
    }

    /**
     * Controlla se è possibile cambiare lo stato corrente e, in caso affermativo, cambia lo stato.
     *
     * @param state L'enumerazione dello stato a cui passare.
     * @return true se lo stato è cambiato con successo, false altrimenti.
     */
    public synchronized boolean checkAndChangeState(CommonState state) {
        if (canChange(state)) {
            usedWithCanChange = true;
            changeState(state);
            usedWithCanChange = false;
            return true;
        }
        return false;
    }

    public synchronized boolean changeToPreviousState() {
        return changeState(previousGlobalState.getState());
    }

    public synchronized GlobalState getCurrentState() {
        if (currentGlobalState == null) {
            Log.error("Non è presente uno stato iniziale");
        }
        return currentGlobalState;
    }

    public synchronized GlobalState getPreviousState() { return previousGlobalState; }

    public void setInitialState(GlobalState startGlobalState) {
        this.currentGlobalState = startGlobalState;
        this.currentGlobalState.startStateTimer();
    }

    // solo per la stampa
    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }
}
