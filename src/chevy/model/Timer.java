package chevy.model;

import chevy.service.Update;
import chevy.service.UpdateManager;

public class Timer implements Update {
    private final double duration;
    private double time = 0d;
    private boolean isRunning = false;
    private boolean delete = true;


    public Timer(double secDuration) {
        this.duration = secDuration;
    }


    public boolean isRunning() {
        return isRunning || !delete;
    }

    /**
     * Fa iniziare l'interpolazione dal punto in cui si è fermata. Se la si usa
     * per la prima volta l'interpolazione partirà dall'inizio.
     */
    public void start() {
        if (duration <= 0d) {
            System.out.println("[!] Il timer non è stato creato perché la sua durata é: " + duration);
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
     * Interrompe l'aggiornamento del timer, ma non in modo perenne. L'interpolazione
     * può essere ripresa in seguito usando la funzione start(), oppure, farla iniziare da capo
     * usando la funzione restart().
     */
    public void stop() {
        isRunning = false;
    }

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
    public boolean updateIsEnd() {
        return delete;
    }
}
