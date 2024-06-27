package chevy.view.animation;

import chevy.service.Render;
import chevy.service.RenderManager;

public class Interpolate implements Render {
    private double time = 0d;
    private boolean isRunning = false;
    private InterpolationTypes interpolationTypes;
    private double start;
    private double end;
    private float duration;
    private boolean delete = true;


    public Interpolate (double start, double end, float duration, InterpolationTypes interpolationTypes) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.interpolationTypes = interpolationTypes;
    }


    public double getValue() {
        return InterpolationFunctions.interpolate(start, end, time, interpolationTypes);
    }

    public void changeStart(double start) {
        this.start = start;
    }

    public void changeEnd(double end) {
        this.end = end;
    }

    public void changeDuration(float duration) {
        this.duration = duration;
    }

    public void changeInterpolationType(InterpolationTypes types) {
        this.interpolationTypes = types;
    }

    /**
     * Fa iniziare l'interpolazione dal punto in cui si è fermata. Se la si usa
     * per la prima volta l'interpolazione partirà dall'inizio.
     */
    public void start() {
        if (duration <= 0d) {
            System.out.println("[!] L'interpolazione non è stata creata perché la sua durata é: " + duration);
            return;
        }

        isRunning = true;

        if (delete) {
            delete = false;
            RenderManager.addToRender(this);
        }
    }

    /**
     * Fa iniziare sempre e comunque l'interpolazione dall'inizio.
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
     * Elimina l'interpolazione, questo vuol dire che non verrà più aggiornata.
     */
    public void delete() {
        stop();
        delete = true;
    }

    /**
     * Interrompe l'aggiornamento dell'interpolazione, ma non in modo perenne. L'interpolazione
     * può essere ripresa in seguito usando la funzione start(), oppure, farla iniziare da capo
     * usando la funzione restart().
     */
    public void stop() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning || !delete;
    }


    @Override
    public void render(double delta) {
        if (isRunning) {
            time += delta / duration;

            if (time >= 1f) {
                time = 1f;
                stop();
            }
        }
    }

    @Override
    public boolean renderIsEnd() {
        return delete;
    }
}
