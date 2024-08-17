package chevy.utils;

import chevy.settings.GameSettings;

public class Log {
    public static void error(String message) {
        if (GameSettings.logLevel.compareTo(Level.ERROR) >= 0) {
            System.err.println("\033[91;1m[x]\033[0m " + message);
        }
    }

    public static void warn(String message) {
        if (GameSettings.logLevel.compareTo(Level.WARN) >= 0) {
            System.out.println("\033[93;1m[!]\033[0m " + message);
        }
    }

    public static void info(String message) {
//        if (GameSettings.logLevel.compareTo(Level.INFO) >= 0) {
//            System.out.println("\033[90;1m[i]\033[0m " + message);
//        }
    }

    /**
     * Livello di verbosità del logging. INFO è il più verboso, ERROR il più terso. OFF lo disabilita completamente.
     */
    public enum Level {
        OFF, ERROR, WARN, INFO
    }
}