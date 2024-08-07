package chevy.model.entity.stateMachine;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

/**
 * Rappresenta uno stato all'interno di una macchina a stati.
 */
public class GlobalState {
    private final CommonState commonState; // L'enumerazione associata a questo stato.
    private final boolean selfEdge; // Indica se questo stato ha un auto-arco.
    private final float duration; // La durata di questo stato.
    // Gli stati collegati a questo stato.
    private final Dictionary<CommonState, GlobalState> linkedStates = new Hashtable<>();
    private final Timer timer;

    /**
     * Costruttore di base. Crea uno stato senza auto-arco e senza durata.
     *
     * @param commonState l'enumerazione associata a questo stato
     */
    public GlobalState(CommonState commonState) {
        this.commonState = commonState;
        this.selfEdge = false;
        this.duration = 0.f;
        this.timer = null;
    }

    /**
     * Costruttore con durata. Crea uno stato senza auto-arco ma con una durata specificata.
     *
     * @param commonState l'enumerazione associata a questo stato
     * @param secDuration      la durata di questo stato in secondi
     */
    public GlobalState(CommonState commonState, float secDuration) {
        this.commonState = commonState;
        this.selfEdge = false;
        this.duration = secDuration;
        this.timer = new Timer(duration);
    }

    /**
     * Costruttore con auto-arco. Crea uno stato con un auto-arco ma senza durata.
     *
     * @param commonState l'enumerazione associata a questo stato
     * @param selfEdge         indica se questo stato ha un auto-arco
     */
    public GlobalState(CommonState commonState, boolean selfEdge) {
        this.commonState = commonState;
        this.selfEdge = selfEdge;
        this.duration = 0.f;
        this.timer = null;
    }

    /**
     * Costruttore completo. Crea uno stato con un auto-arco e una durata specificata.
     *
     * @param commonState l'enumerazione associata a questo stato
     * @param duration         la durata di questo stato in secondi
     * @param selfEdge         indica se questo stato ha un auto-arco
     */
    public GlobalState(CommonState commonState, float duration, boolean selfEdge) {
        this.commonState = commonState;
        this.selfEdge = selfEdge;
        this.duration = duration;
        this.timer = new Timer(this.duration);
    }

    /**
     * Restituisce l'enumerazione associata a questo stato.
     *
     * @return L'enumerazione associata a questo stato
     */
    public CommonState getState() {
        return commonState;
    }

    /**
     * Collega un altro stato a questo stato.
     *
     * @param globalState lo stato da collegare a questo stato
     */
    public void linkState(GlobalState globalState) {
        linkedStates.put(globalState.getState(), globalState);
    }

    /**
     * Trova uno stato collegato allo stato corrente.
     *
     * @param commonEnumStates l'enumerazione dello stato da trovare
     * @return lo stato collegato a questo stato, o null se non esiste
     */
    public GlobalState findLinkedState(CommonState commonEnumStates) {
        if (selfEdge && Objects.equals(this.commonState, commonEnumStates)) {
            return this;
        }
        return linkedStates.get(commonEnumStates);
    }

    /**
     * @return la durata dello stato corrente
     */
    public float getDuration() {
        return duration;
    }

    /**
     * @return true se lo stato è terminato, false altrimenti.
     */
    public boolean isFinished() {
        if (timer == null) return true;

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
        GlobalState globalState1 = (GlobalState) o;
        return Objects.equals(commonState, globalState1.commonState);
    }

    @Override
    public int hashCode() {
        return commonState.hashCode();
    }

    @Override
    public String toString() {
        return commonState.toString();
    }
}