package chevy.model.entity.stateMachine;

import chevy.utils.Log;

/**
 * La StateMachine è una macchina a stati finiti (FSM - Finite State Machine) che gestisce le
 * transizioni tra i
 * diversi stati.
 */
public class StateMachine {
    private Vertex currentVertex;
    private Vertex nextVertex;
    private boolean usedWithCanChange;
    private String name; // solo per capire di chi è la stampa

    /**
     * Cambia lo stato corrente della macchina a stati
     *
     * @param state l'enumerazione dello stato a cui passare
     * @return {@code true} se lo stato è cambiato con successo, {@code false} altrimenti
     */
    public synchronized boolean changeState(EntityState state) {
        if (currentVertex == null) {
            Log.error("Non è presente uno stato iniziale");
            return false;
        }

        // solo per la stampa
        String logMessage = "";
        if (name != null) {
            logMessage += name + ": " + currentVertex;
        }

        if (!usedWithCanChange) {
            nextVertex = currentVertex.findLinkedVertex(state);
        }

        if (nextVertex != null) {
            final Vertex previousVertex = currentVertex;
            currentVertex = nextVertex;

            // solo per la stampa
            if (name != null) {
                Log.info(logMessage + " -> " + nextVertex);
            }

            previousVertex.stopStateTimer();
            currentVertex.startStateTimer();
            nextVertex = null;
            return true;
        }

        // solo per la stampa
        if (name != null) {
            Log.info(logMessage);
        }

        return false;
    }

    /**
     * Controlla se è possibile cambiare lo stato corrente della macchina a stati
     *
     * @param state l'enumerazione dello stato a cui passare
     * @return {@code true} se è possibile cambiare lo stato, {@code false} altrimenti
     */
    public synchronized boolean canChange(EntityState state) {
        if (currentVertex == null) {
            Log.error("Non è presente uno stato iniziale");
            return false;
        }

        nextVertex = currentVertex.findLinkedVertex(state);
        if (nextVertex == null) {
            return false;
        }

        return currentVertex.isFinished();
    }

    /**
     * Controlla se è possibile cambiare lo stato corrente e, in caso affermativo, cambia lo stato
     *
     * @param state l'enumerazione dello stato a cui passare
     * @return {@code true} se lo stato è cambiato con successo, {@code false} altrimenti
     */
    public synchronized boolean checkAndChangeState(EntityState state) {
        if (canChange(state)) {
            usedWithCanChange = true;
            changeState(state);
            usedWithCanChange = false;
            return true;
        }
        return false;
    }

    public synchronized Vertex getCurrentState() {
        if (currentVertex == null) {
            Log.error("Non è presente uno stato iniziale");
        }
        return currentVertex;
    }

    public void setInitialState(Vertex startVertex) {
        currentVertex = startVertex;
        currentVertex.startStateTimer();
    }

    /**
     * Usato nella stanza
     *
     * @param name nome dell'entità a cui è associata la StateMachine
     */
    public void setName(String name) {this.name = name;}
}
