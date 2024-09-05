package chevy.model.entity.stateMachine;

import chevy.utils.Log;

/**
 * La StateMachine è una macchina a stati finiti (FSM - Finite State Machine) che gestisce le transizioni tra i
 * diversi stati.
 */
public class StateMachine {
    private Vertex currentVertex;
    private Vertex previousVertex;
    private Vertex nextVertex;
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
        if (currentVertex == null) {
            Log.error("Non è presente uno stato iniziale");
            return false;
        }

        // solo per la stampa
        String logMessage = "";
        if (stateMachineName != null) {
            logMessage += stateMachineName + ": " + currentVertex;
        }

        if (!usedWithCanChange) {
            nextVertex = currentVertex.findLinkedState(state);
        }

        if (nextVertex != null) {
            previousVertex = currentVertex;
            currentVertex = nextVertex;

            // solo per la stampa
            if (stateMachineName != null) {
                Log.info(logMessage + " -> " + nextVertex);
            }

            previousVertex.stopStateTimer();
            currentVertex.startStateTimer();
            nextVertex = null;
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
        if (currentVertex == null) {
            Log.error("Non è presente uno stato iniziale");
            return false;
        }

        nextVertex = currentVertex.findLinkedState(state);
        if (nextVertex == null) {
            return false;
        }

        return currentVertex.isFinished();
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
        return changeState(previousVertex.getState());
    }

    public synchronized Vertex getCurrentState() {
        if (currentVertex == null) {
            Log.error("Non è presente uno stato iniziale");
        }
        return currentVertex;
    }

    public synchronized Vertex getPreviousState() { return previousVertex; }

    public void setInitialState(Vertex startVertex) {
        this.currentVertex = startVertex;
        this.currentVertex.startStateTimer();
    }

    // solo per la stampa
    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }
}
