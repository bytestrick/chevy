package chevy.model.entity.stateMachine;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Used in the {@link StateMachine}
 */
public class Vertex {
    /**
     * The enumeration associated with this state
     */
    private final EntityState state;
    /**
     * Whether this state has a self-edge
     */
    private final boolean selfEdge;
    /**
     * The duration of this state
     */
    private final float duration;
    /**
     * The states linked to this state
     */
    private final Dictionary<EntityState, Vertex> linkedVertices = new Hashtable<>();
    private final Timer timer;

    /**
     * Create a state without a self-edge and without a duration
     *
     * @param state the enumeration associated with this state
     */
    public Vertex(EntityState state) {
        this.state = state;
        selfEdge = false;
        duration = 0;
        timer = null;
    }

    /**
     * Create a state without a self-edge but with a specified duration
     *
     * @param state       the enumeration associated with this state
     * @param secDuration the duration of this state in seconds
     */
    public Vertex(EntityState state, float secDuration) {
        this.state = state;
        selfEdge = false;
        duration = secDuration;
        timer = new Timer(duration);
    }

    /**
     * Create a state with a self-edge and a specified duration
     *
     * @param state    enumeration associated with this state
     * @param duration the duration of this state
     * @param selfEdge whether this state has a self-edge
     */
    public Vertex(EntityState state, float duration, boolean selfEdge) {
        this.state = state;
        this.selfEdge = selfEdge;
        this.duration = duration;
        timer = new Timer(this.duration);
    }

    /**
     * @return the enumeration associated with this state
     */
    public EntityState getState() {
        return state;
    }

    /**
     * Links another state to this state
     *
     * @param vertex the state to link
     */
    public void linkVertex(Vertex vertex) {
        linkedVertices.put(vertex.getState(), vertex);
    }

    /**
     * Find a state linked to the current state
     *
     * @param state the state to find
     * @return the state linked to this state, o {@code null} if it doesn't exist
     */
    Vertex findLinkedVertex(EntityState state) {
        if (selfEdge && Objects.equals(this.state, state)) {
            return this;
        }
        return linkedVertices.get(state);
    }

    /**
     * @return the duration of this state
     */
    public float getDuration() {
        return duration;
    }

    /**
     * @return {@code true} if the state has ended, {@code false} otherwise
     */
    public boolean isFinished() {
        if (timer == null) {
            return true;
        }
        return !timer.isRunning();
    }

    /**
     * Start the timer of the current state
     */
    void startStateTimer() {
        if (timer != null && !timer.isRunning()) {
            timer.restart();
        }
    }

    void stopStateTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex1 = (Vertex) o;
        return Objects.equals(state, vertex1.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
