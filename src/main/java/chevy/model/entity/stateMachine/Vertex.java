package chevy.model.entity.stateMachine;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Uno stato nella {@link StateMachine}
 */
public class Vertex {
    private final EntityState state; // L'enumerazione associata a questo stato.
    /** Indica se questo stato ha un auto-arco */
    private final boolean selfEdge;
    /** La durata di questo stato */
    private final float duration;
    /** Gli stati collegati a questo stato */
    private final Dictionary<EntityState, Vertex> linkedVertices = new Hashtable<>();
    private final Timer timer;

    /**
     * Crea uno stato senza auto-arco e senza durata
     *
     * @param state l'enumerazione associata a questo stato
     */
    public Vertex(EntityState state) {
        this.state = state;
        selfEdge = false;
        duration = 0;
        timer = null;
    }

    /**
     * Crea uno stato senza auto-arco ma con una durata specificata
     *
     * @param state       l'enumerazione associata a questo stato
     * @param secDuration la durata di questo stato in secondi
     */
    public Vertex(EntityState state, float secDuration) {
        this.state = state;
        selfEdge = false;
        duration = secDuration;
        timer = new Timer(duration);
    }

    /**
     * Crea uno stato con un auto-arco e una durata specificata
     *
     * @param state    l'enumerazione associata a questo stato
     * @param duration la durata di questo stato in secondi
     * @param selfEdge indica se questo stato ha un auto-arco
     */
    public Vertex(EntityState state, float duration, boolean selfEdge) {
        this.state = state;
        this.selfEdge = selfEdge;
        this.duration = duration;
        timer = new Timer(this.duration);
    }

    /**
     * Restituisce l'enumerazione associata a questo stato
     *
     * @return L'enumerazione associata a questo stato
     */
    public EntityState getState() {return state;}

    /**
     * Collega un altro stato a questo stato
     *
     * @param vertex lo stato da collegare a questo stato
     */
    public void linkVertex(Vertex vertex) {linkedVertices.put(vertex.getState(), vertex);}

    /**
     * Trova uno stato collegato allo stato corrente
     *
     * @param state l'enumerazione dello stato da trovare
     * @return lo stato collegato a questo stato, o {@code null} se non esiste
     */
    Vertex findLinkedVertex(EntityState state) {
        if (selfEdge && Objects.equals(this.state, state)) {
            return this;
        }
        return linkedVertices.get(state);
    }

    /**
     * @return la durata dello stato corrente
     */
    public float getDuration() {return duration;}

    /**
     * @return {@code true} se lo stato è terminato, {@code false} altrimenti
     */
    public boolean isFinished() {
        if (timer == null) {
            return true;
        }
        return !timer.isRunning();
    }

    /**
     * Avvia il timer dello stato corrente
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
    public int hashCode() {return state.hashCode();}

    @Override
    public String toString() {return state.toString();}
}
