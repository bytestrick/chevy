package chevy.utils;

import chevy.settings.GameSettings;

/**
 * Gestione dei messaggi di logging
 */
public class Log {
    /**
     * Registra un evento di errore. Un evento simile indica un punto in cui l'applicazione non può continuare.
     * Quindi la chiamata è spesso seguita da System.exit(1).
     *
     * @param message il messaggio diagnostico che descrive l'evento.
     */
    public static void error(String message) {
        if (GameSettings.logLevel.compareTo(Level.ERROR) >= 0) {
            System.err.println("\033[91;1m[x]\033[0m " + message);
        }
    }

    /**
     * Registra un evento grave ma non fatale. Un evento simile causa malfunzionamenti ma non termina l'applicazione.
     *
     * @param message il messaggio diagnostico che descrive l'evento.
     */
    public static void warn(String message) {
        if (GameSettings.logLevel.compareTo(Level.WARN) >= 0) {
            System.out.println("\033[93;1m[!]\033[0m " + message);
        }
    }

    /**
     * Registra un evento normale. È il tipo di evento più comune in un'applicazione, come il cambiamento di uno stato,
     * di una scena, la terminazione di un thread, ...
     *
     * @param message il messaggio diagnostico che descrive l'evento.
     */
    public static void info(String message) {
        if (GameSettings.logLevel.compareTo(Level.INFO) >= 0) {
            System.out.println("\033[90;1m[i]\033[0m " + message);
        }
    }

    /**
     * Livello di verbosità del logging. INFO è il più verboso, ERROR il più terso. OFF lo disabilita completamente.
     * Questo parametro è modificabile dalle opzioni di gioco.
     */
    public enum Level {
        OFF, ERROR, WARN, INFO
    }
}