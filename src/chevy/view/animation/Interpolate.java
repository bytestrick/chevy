package chevy.view.animation;

import chevy.service.Render;
import chevy.service.RenderManager;

/**
 * La classe Interpolate implementa l'interfaccia Render e gestisce l'interpolazione dei valori nel gioco.
 * Questa classe contiene metodi per gestire lo stato dell'interpolazione, come l'avvio, l'arresto e
 * l'aggiornamento dell'interpolazione.
 */
public class Interpolate implements Render {
    // Tempo trascorso dall'inizio dell'interpolazione.
    private double time = 0d;
    // Se l'interpolazione è attualmente in esecuzione.
    private boolean isRunning = false;
    // Tipo di interpolazione da utilizzare.
    private InterpolationTypes interpolationTypes;
    // Valore iniziale dell'interpolazione.
    private double start;
    // Valore finale dell'interpolazione.
    private double end;
    // Durata totale dell'interpolazione.
    private float duration;
    // Se l'interpolazione deve essere eliminata.
    private boolean isDelete = true;

    /**
     * Costruttore della classe Interpolate. Inizializza l'oggetto con i valori di inizio e fine,
     * la durata e il tipo di interpolazione.
     */
    public Interpolate (double start, double end, float duration, InterpolationTypes interpolationTypes) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.interpolationTypes = interpolationTypes;
    }

    /**
     * Restituisce il valore corrente dell'interpolazione.
     */
    public double getValue() {
//        time = (double) Math.round(time * 100) / 100;
        return InterpolationFunctions.interpolate(start, end, time, interpolationTypes);
    }

    /**
     * Cambia il valore di inizio dell'interpolazione.
     */
    public void changeStart(double start) {
        this.start = start;
    }

    /**
     * Cambia il valore finale dell'interpolazione.
     */
    public void changeEnd(double end) {
        this.end = end;
    }

    /**
     * Cambia la durata dell'interpolazione.
     */
    public void changeDuration(float duration) {
        this.duration = duration;
    }

    /**
     * Cambia il tipo di interpolazione.
     */
    public void changeInterpolationType(InterpolationTypes types) {
        this.interpolationTypes = types;
    }

    /**
     * Avvia l'interpolazione dal punto in cui si è fermata. Se è la prima volta che viene utilizzata,
     * l'interpolazione partirà dall'inizio.
     */
    public void start() {
        if (duration <= 0d) {
            System.out.println("[!] L'interpolazione non è stata creata perché la sua durata é: " + duration);
            return;
        }

        isRunning = true;

        if (isDelete) {
            isDelete = false;
            RenderManager.addToRender(this);
        }
    }

    /**
     * Riavvia sempre l'interpolazione dall'inizio.
     */
    public void restart() {
        time = 0d;
        if (isDelete) {
            start();
            return;
        }
        isRunning = true;
    }

    /**
     * Elimina l'interpolazione, questo significa che non verrà più aggiornata.
     */
    public void delete() {
        stop();
        isDelete = true;
    }

    /**
     * Interrompe l'aggiornamento dell'interpolazione, ma non in modo permanente. L'interpolazione può essere
     * ripresa in seguito usando la funzione start(), oppure, può essere fatta ripartire da capo usando
     * la funzione restart().
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Restituisce se l'interpolazione è attualmente in esecuzione.
     */
    public boolean isRunning() {
        return isRunning && !isDelete;
    }

    /**
     * Aggiorna l'interpolazione in base al tempo trascorso.
     */
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

    /**
     * Restituisce se l'interpolazione è terminata.
     */
    @Override
    public boolean renderIsEnd() {
        return isDelete;
    }
}
