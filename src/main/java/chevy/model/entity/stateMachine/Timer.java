package chevy.model.entity.stateMachine;

import chevy.service.Updatable;
import chevy.service.UpdateManager;
import chevy.utils.Log;

/**
 * Tiene traccia del tempo trascorso
 */
public class Timer implements Updatable {
    private final double duration;
    private double time;
    private boolean running;
    private boolean delete = true;

    Timer(double secDuration) {duration = secDuration;}

    public boolean isRunning() {return running && !delete;}

    /**
     * Fa iniziare l'interpolazione dal punto in cui si è fermata. Se la si usa
     * per la prima volta l'interpolazione partirà dall'inizio.
     */
    private void start() {
        if (duration <= 0d) {
            Log.warn("Il timer non è stato creato perché la sua durata è: " + duration);
            return;
        }

        running = true;

        if (delete) {
            delete = false;
            UpdateManager.register(this);
        }
    }

    /**
     * Fa iniziare sempre e comunque il timer dall'inizio
     */
    public void restart() {
        time = 0d;
        if (delete) {
            start();
            return;
        }
        running = true;
    }

    /**
     * Interrompe l'aggiornamento del timer, ma non in modo permanente. L'interpolazione
     * può essere ripresa in seguito usando la {@link #start()}, oppure, farla iniziare da capo
     * usando {@link #restart()}.
     */
    public void stop() {running = false;}

    /**
     * Elimina il timer, questo vuol dire che non verrà più aggiornata.
     */
    private void delete() {
        stop();
        delete = true;
    }

    @Override
    public void update(double delta) {
        if (running) {
            time += delta;

            if (time >= duration) {
                time = duration;
                running = false;
                delete();
            }
        }
    }

    @Override
    public boolean updateFinished() {return delete;}
}
