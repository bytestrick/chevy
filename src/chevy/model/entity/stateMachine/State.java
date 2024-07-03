package chevy.model.entity.stateMachine;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Rappresenta uno stato all'interno di una macchina a stati.
 */
public class State {
    private final CommonEnumStates commonEnumStates; // L'enumerazione associata a questo stato.
    private final boolean selfEdge; // Indica se questo stato ha un auto-arco.
    private final float durationState; // La durata di questo stato.
    // Gli stati collegati a questo stato.
    private final Dictionary<CommonEnumStates, State> linkedStates = new Hashtable<>();
    private final Timer stateTimer;

    /**
     * Costruttore di base. Crea uno stato senza auto-arco e senza durata.
     *
     * @param commonEnumStates l'enumerazione associata a questo stato
     */
    public State(CommonEnumStates commonEnumStates) {
        this.commonEnumStates = commonEnumStates;
        this.selfEdge = false;
        this.durationState = 0.f;
        this.stateTimer = null;
    }

    /**
     * Costruttore con durata. Crea uno stato senza auto-arco ma con una durata specificata.
     *
     * @param commonEnumStates l'enumerazione associata a questo stato
     * @param secDuration      la durata di questo stato in secondi
     */
    public State(CommonEnumStates commonEnumStates, float secDuration) {
        this.commonEnumStates = commonEnumStates;
        this.selfEdge = false;
        this.durationState = secDuration;
        this.stateTimer = new Timer(durationState);
    }

    /**
     * Costruttore con auto-arco. Crea uno stato con un auto-arco ma senza durata.
     *
     * @param commonEnumStates l'enumerazione associata a questo stato
     * @param selfEdge         indica se questo stato ha un auto-arco
     */
    public State(CommonEnumStates commonEnumStates, boolean selfEdge) {
        this.commonEnumStates = commonEnumStates;
        this.selfEdge = selfEdge;
        this.durationState = 0.f;
        this.stateTimer = null;
    }

    /**
     * Costruttore completo. Crea uno stato con un auto-arco e una durata specificata.
     *
     * @param commonEnumStates l'enumerazione associata a questo stato
     * @param duration         la durata di questo stato in secondi
     * @param selfEdge         indica se questo stato ha un auto-arco
     */
    public State(CommonEnumStates commonEnumStates, float duration, boolean selfEdge) {
        this.commonEnumStates = commonEnumStates;
        this.selfEdge = selfEdge;
        this.durationState = duration;
        this.stateTimer = new Timer(durationState);
    }

    /**
     * Restituisce l'enumerazione associata a questo stato.
     *
     * @return L'enumerazione associata a questo stato
     */
    public CommonEnumStates getStateEnum() {
        return commonEnumStates;
    }

    /**
     * Collega un altro stato a questo stato.
     *
     * @param state lo stato da collegare a questo stato
     */
    public void linkState(State state) {
        linkedStates.put(state.getStateEnum(), state);
    }

    /**
     * Trova uno stato collegato allo stato corrente.
     *
     * @param commonEnumStates l'enumerazione dello stato da trovare
     * @return lo stato collegato a questo stato, o null se non esiste
     */
    public State findLinkedState(CommonEnumStates commonEnumStates) {
        if (selfEdge && Objects.equals(this.commonEnumStates, commonEnumStates)) {
            return this;
        }
        return linkedStates.get(commonEnumStates);
    }

    /**
     * @return la durata dello stato corrente
     */
    public float getDuration() {
        return durationState;
    }

    /**
     * @return true se lo stato è terminato, false altrimenti.
     */
    public boolean isFinished() {
        if (stateTimer == null) return true;

        return !stateTimer.isRunning();
    }

    /**
     * Avvia il timer dello stato corrente.
     */
    public void startStateTimer() {
        if (stateTimer != null && !stateTimer.isRunning()) {
            stateTimer.restart();
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

        State state1 = (State) o;

        return Objects.equals(commonEnumStates, state1.commonEnumStates);
    }

    @Override
    public int hashCode() {
        return commonEnumStates.hashCode();
    }

    @Override
    public String toString() {
        return commonEnumStates.toString();
    }
}