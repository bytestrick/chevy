package chevy.model.entity.stateMachine;

import chevy.utils.Log;

/**
 * La classe StateMachine rappresenta una macchina a stati finiti (FSM - Finite GlobalState Machine)
 * che gestisce le transizioni tra i diversi stati.
 * Questa classe consente di definire un comportamento strutturato in cui un entità dinamica può trovarsi
 * in uno e un solo stato alla volta, cambiando stato in risposta a determinati eventi o condizioni.
 * Prima di usare la macchina a stati finiti è necessario definire uno stato di partenza. Successivamente è
 * possibile cambiare stato attraverso il metodo checkAndChangeState(), oppure usando in modo combinato
 * il metodo canChange() e changeState(). Se si usa solo changeState() non si controlla che lo stato corrente
 * sia finito.
 */
public class StateMachine {
    private GlobalState currentGlobalState;
    private GlobalState previousGlobalState;
    private GlobalState nextGlobalState;
    private boolean usedWithCanChange = false;
    private String stateMachineName; // solo per capire di chi è la stampa


    public StateMachine() {}

    /**
     * Cambia lo stato corrente della macchina a stati.
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

        if (!usedWithCanChange)
            nextGlobalState = currentGlobalState.findLinkedState(state);

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