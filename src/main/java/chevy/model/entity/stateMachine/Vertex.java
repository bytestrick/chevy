package chevy.model.entity.stateMachine;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Uno stato nella {@link StateMachine}
 */
public class Vertex {
    private final CommonState state; // L'enumerazione associata a questo stato.
    /** Indica se questo stato ha un auto-arco. */
    private final boolean selfEdge;
    /** La durata di questo stato. */
    private final float duration;
    /** Gli stati collegati a questo stato. */
    private final Dictionary<CommonState, Vertex> linkedVertices = new Hashtable<>();
    private final Timer timer;

    /**
     * Costruttore di base. Crea uno stato senza auto-arco e senza durata.
     *
     * @param state l'enumerazione associata a questo stato
     */
    public Vertex(CommonState state) {
        this.state = state;
        this.selfEdge = false;
        this.duration = 0.f;
        this.timer = null;
    }

    /**
     * Costruttore con durata. Crea uno stato senza auto-arco ma con una durata specificata.
     *
     * @param state l'enumerazione associata a questo stato
     * @param secDuration la durata di questo stato in secondi
     */
    public Vertex(CommonState state, float secDuration) {
        this.state = state;
        this.selfEdge = false;
        this.duration = secDuration;
        this.timer = new Timer(duration);
    }

    /**
     * Costruttore con auto-arco. Crea uno stato con un auto-arco ma senza durata.
     *
     * @param state l'enumerazione associata a questo stato
     * @param selfEdge    indica se questo stato ha un auto-arco
     */
    public Vertex(CommonState state, boolean selfEdge) {
        this.state = state;
        this.selfEdge = selfEdge;
        this.duration = 0.f;
        this.timer = null;
    }

    /**
     * Costruttore completo. Crea uno stato con un auto-arco e una durata specificata.
     *
     * @param state l'enumerazione associata a questo stato
     * @param duration    la durata di questo stato in secondi
     * @param selfEdge    indica se questo stato ha un auto-arco
     */
    public Vertex(CommonState state, float duration, boolean selfEdge) {
        this.state = state;
        this.selfEdge = selfEdge;
        this.duration = duration;
        this.timer = new Timer(this.duration);
    }

    /**
     * Restituisce l'enumerazione associata a questo stato.
     *
     * @return L'enumerazione associata a questo stato
     */
    public CommonState getState() { return state; }

    /**
     * Collega un altro stato a questo stato.
     *
     * @param vertex lo stato da collegare a questo stato
     */
    public void linkVertex(Vertex vertex) {
        linkedVertices.put(vertex.getState(), vertex);
    }

    /**
     * Trova uno stato collegato allo stato corrente.
     *
     * @param commonEnumStates l'enumerazione dello stato da trovare
     * @return lo stato collegato a questo stato, o null se non esiste
     */
    public Vertex findLinkedVertex(CommonState commonEnumStates) {
        if (selfEdge && Objects.equals(this.state, commonEnumStates)) {
            return this;
        }
        return linkedVertices.get(commonEnumStates);
    }

    /**
     * @return la durata dello stato corrente
     */
    public float getDuration() { return duration; }

    /**
     * @return true se lo stato è terminato, false altrimenti.
     */
    public boolean isFinished() {
        if (timer == null) {
            return true;
        }
        return !timer.isRunning();
    }

    /**
     * Avvia il timer dello stato corrente.
     */
    public void startStateTimer() {
        if (timer != null && !timer.isRunning()) {
            timer.restart();
        }
    }

    public void stopStateTimer() {
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
    public int hashCode() { return state.hashCode(); }

    @Override
    public String toString() { return state.toString(); }
}