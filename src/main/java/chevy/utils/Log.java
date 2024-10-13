package chevy.utils;

/**
 * Messaggi diagnostici
 */
public final class Log {
    /** Valore globale per la granularità della diagnostica */
    public static Log.Level logLevel = Level.INFO;

    /**
     * Registra un evento di errore. Un evento simile indica un punto in cui l'applicazione non
     * può continuare. Quindi la chiamata è spesso seguita da <code>System.exit(1)</code>.
     *
     * @param message il messaggio diagnostico che descrive l'evento
     */
    public static void error(String message) {
        if (Level.ERROR.compareTo(logLevel) >= 0) {
            System.err.println("\033[91;1m[x]\033[0m " + message);
        }
    }

    /**
     * Registra un evento grave ma non fatale. Un evento simile causa malfunzionamenti ma non
     * termina l'applicazione.
     *
     * @param message il messaggio diagnostico che descrive l'evento
     */
    public static void warn(String message) {
        if (Level.WARN.compareTo(logLevel) >= 0) {
            System.out.println("\033[93;1m[!]\033[0m " + message);
        }
    }

    /**
     * Registra un evento normale. È il tipo di evento più comune in un'applicazione, come il
     * cambiamento di uno stato, di una scena, la terminazione di un thread, ...
     *
     * @param message il messaggio diagnostico che descrive l'evento
     */
    public static void info(String message) {
        if (Level.INFO.compareTo(logLevel) == 0) {
            System.out.println("\033[90;1m[i]\033[0m " + message);
        }
    }

    /**
     * Granularità della diagnostica
     * <p>
     * {@link #INFO} è il più verboso, {@link #ERROR} il più terso. {@link #OFF} lo disabilita
     * completamente.
     */
    public enum Level {INFO, WARN, ERROR, OFF}
}