package chevy.model.entity.stateMachine;

import chevy.utils.Log;

/**
 * A finite state machine (FSM) that manages transitions between different states.
 */
public class StateMachine {
    private Vertex currentVertex;
    private Vertex nextVertex;
    private boolean usedWithCanChange;
    private String name; // only for logging

    /**
     * Change the current state of the state machine
     *
     * @param state the state to change to
     * @return {@code true} if the change was successful, {@code false} otherwise
     */
    public synchronized boolean changeState(EntityState state) {
        if (currentVertex == null) {
            Log.error("There is no initial state");
            return false;
        }

        String logMessage = ""; // for logging
        if (name != null) {
            logMessage += name + ": " + currentVertex;
        }

        if (!usedWithCanChange) {
            nextVertex = currentVertex.findLinkedVertex(state);
        }

        if (nextVertex != null) {
            final Vertex previousVertex = currentVertex;
            currentVertex = nextVertex;

            // logging
            if (name != null) {
                Log.info(logMessage + " -> " + nextVertex);
            }

            previousVertex.stopStateTimer();
            currentVertex.startStateTimer();
            nextVertex = null;
            return true;
        }

        // logging
        if (name != null) {
            Log.info(logMessage);
        }

        return false;
    }

    /**
     * Check if it is possible to change the current state of the state machine
     *
     * @param state the state to change to
     * @return {@code true} if the state change can happen, {@code false} otherwise
     */
    public synchronized boolean canChange(EntityState state) {
        if (currentVertex == null) {
            Log.error("There is no initial state");
            return false;
        }

        nextVertex = currentVertex.findLinkedVertex(state);
        if (nextVertex == null) {
            return false;
        }

        return currentVertex.isFinished();
    }

    /**
     * Check if it is possible to change the current state of the state machine and, if so, change the state
     *
     * @param state the state to change to
     * @return {@code true} if the change of state happened, {@code false} otherwise
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
            Log.error("There is no initial state");
        }
        return currentVertex;
    }

    public void setInitialState(Vertex startVertex) {
        currentVertex = startVertex;
        currentVertex.startStateTimer();
    }

    /**
     * @param name name of the entity that uses the state machine
     */
    public void setName(String name) {
        this.name = name;
    }
}
