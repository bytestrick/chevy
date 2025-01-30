package chevy.utils;

/**
 * Logging
 */
public final class Log {
    /**
     * Global value for diagnostic granularity
     */
    public static Log.Level logLevel = Level.OFF;

    /**
     * Logs an error event. Such an event indicates a point where the application cannot continue.
     * Therefore, the call is often followed by {@code System.exit(1)}.
     *
     * @param message the diagnostic message that describes the event
     */
    public static void error(String message) {
        if (Level.ERROR.compareTo(logLevel) >= 0) {
            System.err.println("\033[91;1m[x]\033[0m " + message);
        }
    }

    /**
     * Logs a severe but non-fatal event. Such an event causes malfunctions but does not terminate the application.
     *
     * @param message the diagnostic message that describes the event
     */
    public static void warn(String message) {
        if (Level.WARN.compareTo(logLevel) >= 0) {
            System.out.println("\033[93;1m[!]\033[0m " + message);
        }
    }

    /**
     * Logs a normal event. It is the most common type of event in an application, such as changing a state, a scene, terminating a thread, ...
     *
     * @param message the diagnostic message that describes the event
     */
    public static void info(String message) {
        if (Level.INFO.compareTo(logLevel) == 0) {
            System.out.println("\033[90;1m[i]\033[0m " + message);
        }
    }

    /**
     * Diagnostic granularity
     * <p>
     * {@link #INFO} is the most verbose, {@link #ERROR} is the most terse. {@link #OFF} disables it completely
     */
    public enum Level {INFO, WARN, ERROR, OFF}
}
