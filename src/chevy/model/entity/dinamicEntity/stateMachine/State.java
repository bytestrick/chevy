package chevy.model.entity.dinamicEntity.stateMachine;

import chevy.model.Timer;
import java.util.*;

/**
 * La classe State rappresenta uno stato all'interno di una macchina a stati.
 */
public class State {
    private final CommonEnumStates commonEnumStates; // L'enumerazione associata a questo stato.
    private final boolean selfLoop; // Indica se questo stato ha un auto-arco.
    private final float durationState; // La durata di questo stato.
    private final Dictionary<CommonEnumStates, State> linkedStates = new Hashtable<>(); // Gli stati collegati a questo stato.
    private final Timer stateTimer;

    /**
     * Costruttore di base. Crea uno stato senza auto-arco e senza durata.
     * @param commonEnumStates L'enumerazione associata a questo stato.
     */
    public State(CommonEnumStates commonEnumStates) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = false;
        this.durationState = 0.f;
        this.stateTimer = null;
    }

    /**
     * Costruttore con durata. Crea uno stato senza auto-arco ma con una durata specificata.
     * @param commonEnumStates L'enumerazione associata a questo stato.
     * @param secDuration La durata di questo stato in secondi.
     */
    public State(CommonEnumStates commonEnumStates, float secDuration) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = false;
        this.durationState = secDuration;
        this.stateTimer = new Timer(durationState);
    }

    /**
     * Costruttore con auto-arco. Crea uno stato con un auto-arco ma senza durata.
     * @param commonEnumStates L'enumerazione associata a questo stato.
     * @param selfLoop Indica se questo stato ha un auto-arco.
     */
    public State(CommonEnumStates commonEnumStates, boolean selfLoop) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = selfLoop;
        this.durationState = 0.f;
        this.stateTimer = null;
    }

    /**
     * Costruttore completo. Crea uno stato con un auto-arco e una durata specificata.
     * @param commonEnumStates L'enumerazione associata a questo stato.
     * @param duration La durata di questo stato in secondi.
     * @param selfLoop Indica se questo stato ha un auto-arco.
     */
    public State(CommonEnumStates commonEnumStates, float duration, boolean selfLoop) {
        this.commonEnumStates = commonEnumStates;
        this.selfLoop = selfLoop;
        this.durationState = duration;
        this.stateTimer = new Timer(durationState);
    }

    /**
     * Restituisce l'enumerazione associata a questo stato.
     * @return L'enumerazione associata a questo stato.
     */
    public CommonEnumStates getStateEnum() {
        return commonEnumStates;
    }

    /**
     * Collega un altro stato a questo stato.
     * @param state Lo stato da collegare a questo stato.
     */
    public void linkState(State state) {
        linkedStates.put(state.getStateEnum(), state);
    }

    /**
     * Trova uno stato collegato allo stato corrente.
     * @param commonEnumStates L'enumerazione dello stato da trovare.
     * @return Lo stato collegato a questo stato, o null se non esiste.
     */
    public State findLinkedState(CommonEnumStates commonEnumStates) {
        if (selfLoop && Objects.equals(this.commonEnumStates, commonEnumStates))
            return this;
        return linkedStates.get(commonEnumStates);
    }

    /**
     * Restituisce la durata dello stato corrente.
     * @return La durata dello stato corrente.
     */
    public float getDuration() {
        return durationState;
    }

    /**
     * Controlla se lo stato è terminato.
     * @return true se lo stato è terminato, false altrimenti.
     */
    public boolean isFinished() {
        if (stateTimer == null)
            return true;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

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
