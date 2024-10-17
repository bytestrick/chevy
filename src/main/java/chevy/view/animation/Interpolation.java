package chevy.view.animation;

import chevy.service.RenderManager;
import chevy.service.Renderable;
import chevy.utils.Log;

/**
 * Interpolazione di valori
 */
public final class Interpolation implements Renderable {
    /** Tipo di interpolazione da utilizzare */
    private final Type type;
    /** Tempo trascorso dall'inizio dell'interpolazione */
    private double time;
    /** Se l'interpolazione è attualmente in esecuzione */
    private boolean isRunning;
    /** Valore iniziale dell'interpolazione */
    private double start;
    /** Valore finale dell'interpolazione */
    private double end;
    /** Durata totale dell'interpolazione */
    private float duration;
    /** Se l'interpolazione deve essere eliminata */
    private boolean delete = true;

    public Interpolation(double start, double end, float duration, Type type) {
        this.start = start;
        this.end = end;
        this.duration = duration;
        this.type = type;
    }

    private static double linearInterpolation(double a, double b, double t) {
        return (1 - t) * a + t * b; // 0 <= t <= 1
    }

    private static double easeOutBounce(double t) {
        final double n1 = 7.5625f;
        final double d1 = 2.75f;

        if (t < 1 / d1) {
            return n1 * t * t;
        } else if (t < 2 / d1) {
            return (n1 * (t -= 1.5f / d1) * t + 0.75f);
        } else if (t < 2.5f / d1) {
            return (n1 * (t -= 2.25f / d1) * t + 0.9375f);
        } else {
            return (n1 * (t -= 2.625f / d1) * t + 0.984375f);
        }
    }

    private static double interpolate(Type type, double b, double t, double a) {
        return switch (type) {
            case EASE_IN_SINE -> {
                final double x = 1 - Math.cos((t * Math.PI) / 2);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_SINE -> {
                final double x = Math.sin((t * Math.PI) / 2);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_SINE -> {
                final double x = -1 * (Math.cos(Math.PI * t) - 1) / 2;
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_QUAD -> {
                final double x = t * t;
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_QUAD -> {
                final double x = 1 - (1 - t) * (1 - t);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_QUAD -> {
                final double x = t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_CUBIC -> {
                final double x = Math.pow(t, 3);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_CUBIC -> {
                final double x = 1 - Math.pow(1 - t, 3);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_CUBIC -> {
                final double x = t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_QUART -> {
                final double x = Math.pow(t, 4);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_QUART -> {
                final double x = 1 - Math.pow(1 - t, 4);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_QUART -> {
                final double x = t < 0.5 ? 8 * t * t * t * t : (1 - Math.pow(-2 * t + 2, 4) / 2);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_QUINT -> {
                final double x = Math.pow(t, 5);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_QUINT -> {
                final double x = 1 - Math.pow(1 - t, 5);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_QUINT -> {
                final double x = t < 0.5 ? 16 * t * t * t * t * t :
                        (1 - Math.pow(-2 * t + 2, 5) / 2);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_EXPO -> {
                final double x = t == 0 ? 0 : Math.pow(2, 10 * t - 10);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_EXPO -> {
                final double x = t == 1 ? 1 : (1 - Math.pow(2, -10 * t));
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_EXPO -> {
                final double x = t == 0 ? 0 : t == 1 ? 1 : t < 0.5 ?
                        (Math.pow(2, 20 * t - 10) / 2) :
                        ((2 - Math.pow(2, -20 * t + 10)) / 2);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_CIRC -> {
                final double x = 1 - Math.sqrt(1 - Math.pow(t, 2));
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_CIRC -> {
                final double x = Math.sqrt(1 - Math.pow(t - 1, 2));
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_CIRC -> {
                final double x = t < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2 :
                        (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2;
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_BACK -> {
                final double c1 = 1.70158;
                final double c3 = c1 + 1;
                final double x = c3 * Math.pow(t, 3) - c1 * Math.pow(t, 2);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_BACK -> {
                final double c1 = 1.70158;
                final double c3 = c1 + 1;
                final double x = 1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2);
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_BACK -> {
                final double c1 = 1.70158;
                final double c2 = c1 * 1.525;
                final double x = t < 0.5 ? (Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2 :
                        (Math.pow(2 * t - 2,
                                2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2;
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_ELASTIC -> {
                final double c4 = (2 * Math.PI) / 3;
                final double x = t == 0 ? 0 : t == 1 ? 1 :
                        -1 * Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_ELASTIC -> {
                final double c4 = (2 * Math.PI) / 3;
                final double x = t == 0 ? 0 : t == 1 ? 1 :
                        Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1;
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_OUT_ELASTIC -> {
                final double c5 = (2 * Math.PI) / 4.5;
                final double c6 = Math.sin((20 * t - 11.125) * c5);
                final double x = t == 0 ? 0 : t == 1 ? 1 : t < 0.5 ? -1 * (Math.pow(2,
                        20 * t - 10) * c6) / 2 :
                        (Math.pow(2, -20 * t + 10) * c6) / 2 + 1;
                yield linearInterpolation(a, b, x);
            }
            case EASE_IN_BOUNCE -> {
                final double x = 1 - easeOutBounce(1 - t);
                yield linearInterpolation(a, b, x);
            }
            case EASE_OUT_BOUNCE -> linearInterpolation(a, b, easeOutBounce(t));
            case EASE_IN_OUT_BOUNCE -> {
                final double x = t < 0.5 ? (1 - easeOutBounce(t)) / 2 :
                        (1 + easeOutBounce(t - 1)) / 2;
                yield linearInterpolation(a, b, x);
            }
            case LINEAR -> linearInterpolation(a, b, t);
        };
    }

    /**
     * Avvia l'interpolazione dal punto in cui si è fermata.
     * Se è la prima volta che viene utilizzata, l'interpolazione partirà dall'inizio.
     */
    public void start() {
        if (duration <= 0d) {
            Log.warn("L'interpolazione non è stata creata perché la sua durata è: " + duration);
            return;
        }

        isRunning = true;

        if (delete) {
            delete = false;
            RenderManager.register(this);
        }
    }

    /**
     * Riavvia sempre l'interpolazione dall'inizio.
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
     * Elimina l'interpolazione, questo significa che non verrà più aggiornata.
     */
    public void delete() {
        stop();
        delete = true;
    }

    /**
     * Interrompe l'aggiornamento dell'interpolazione, ma non in modo permanente.
     * L'interpolazione può essere ripresa in seguito usando {@link #start()}, oppure, può
     * essere fatta ripartire da capo usando {@link #restart()}
     */
    private void stop() {isRunning = false;}

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
     * @return il valore corrente dell'interpolazione
     */
    public double getValue() {return interpolate(type, end, time, start);}

    /**
     * @param start imposta il valore di inizio dell'interpolazione
     */
    public void changeStart(double start) {this.start = start;}

    /**
     * @param end imposta il valore finale dell'interpolazione
     */
    public void changeEnd(double end) {this.end = end;}

    /**
     * @param duration imposta la durata dell'interpolazione
     */
    public void changeDuration(float duration) {this.duration = duration;}

    /**
     * @return {@code true} se l'interpolazione è terminata, {@code false} altrimenti
     */
    @Override
    public boolean renderFinished() {return delete;}

    public enum Type {
        LINEAR, EASE_IN_SINE, EASE_OUT_SINE, EASE_IN_OUT_SINE, EASE_IN_QUAD, EASE_OUT_QUAD,
        EASE_IN_OUT_QUAD,
        EASE_IN_CUBIC, EASE_OUT_CUBIC, EASE_IN_OUT_CUBIC, EASE_IN_QUART, EASE_OUT_QUART,
        EASE_IN_OUT_QUART,
        EASE_IN_QUINT, EASE_OUT_QUINT, EASE_IN_OUT_QUINT, EASE_IN_EXPO, EASE_OUT_EXPO,
        EASE_IN_OUT_EXPO, EASE_IN_CIRC, EASE_OUT_CIRC, EASE_IN_OUT_CIRC, EASE_IN_BACK,
        EASE_OUT_BACK, EASE_IN_OUT_BACK, EASE_IN_ELASTIC,
        EASE_OUT_ELASTIC, EASE_IN_OUT_ELASTIC, EASE_IN_BOUNCE, EASE_OUT_BOUNCE, EASE_IN_OUT_BOUNCE
    }
}