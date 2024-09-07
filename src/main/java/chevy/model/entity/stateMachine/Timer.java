package chevy.model.entity.stateMachine;

import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.utils.Log;

/**
 * Tiene traccia del tempo trascorso
 */
public class Timer implements Update {
    private final double duration;
    private double time;
    private boolean isRunning;
    private boolean delete = true;

    public Timer(double secDuration) {this.duration = secDuration;}

    public boolean isRunning() {return isRunning && !delete;}

    /**
     * Fa iniziare l'interpolazione dal punto in cui si è fermata. Se la si usa
     * per la prima volta l'interpolazione partirà dall'inizio.
     */
    public void start() {
        if (duration <= 0d) {
            Log.warn("Il timer non è stato creato perché la sua durata è: " + duration);
            return;
        }

        isRunning = true;

        if (delete) {
            delete = false;
            UpdateManager.addToUpdate(this);
        }
    }

    /**
     * Fa iniziare sempre e comunque il timer dall'inizio.
     */
    public void restart() {
        time = 0d;
        if (delete) {
            start();
            return;
        }
        isRunning = true;
    }

    /**
     * Interrompe l'aggiornamento del timer, ma non in modo permanente. L'interpolazione
     * può essere ripresa in seguito usando la funzione start(), oppure, farla iniziare da capo
     * usando la funzione restart().
     */
    public void stop() {isRunning = false;}

    /**
     * Elimina il timer, questo vuol dire che non verrà più aggiornata.
     */
    public void delete() {
        stop();
        delete = true;
    }

    @Override
    public void update(double delta) {
        if (isRunning) {
            time += delta;

            if (time >= duration) {
                time = duration;
                isRunning = false;
                delete();
            }
        }
    }

    @Override
    public boolean updateFinished() {return delete;}
}