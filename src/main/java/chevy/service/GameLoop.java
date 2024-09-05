package chevy.service;

import chevy.utils.Log;

import java.awt.Toolkit;

/**
 * Il thread che aggiorna il gioco
 */
public final class GameLoop {
    /** FPS ottimali del gioco */
    public static final int TARGET_FRAME_RATE = 60;
    private static final Thread.Builder.OfPlatform worker =
            Thread.ofPlatform().priority(Thread.MAX_PRIORITY);
    private static final Object mutex = new Object();
    private static boolean isRunning;
    private static boolean isPaused;
    /** Serve a tenere traccia del tempo di gioco */
    private static long timeStarted;

    /**
     * La prima volta crea il {@link #worker} e lo avvia. Tutte le successiva chiamate
     * interrompono la pausa.
     */
    public static void start() {
        synchronized (mutex) {
            if (!isRunning) {
                timeStarted = System.currentTimeMillis();
                isRunning = true;
                worker.start(GameLoop::run);
            } else if (isPaused) {
                timeStarted = System.currentTimeMillis();
                isPaused = false;
                mutex.notify();
            }
        }
    }

    /**
     * Mette il {@link #worker} stato di attesa
     */
    public static void stop() {
        synchronized (mutex) {
            if (!isPaused) {
                isPaused = true;
                mutex.notify();
                // narrowing conversion
                Integer value = Long.valueOf(System.currentTimeMillis() - timeStarted).intValue();
                Data.increase("stats.time.played.count", value);
            }
        }
    }

    /**
     * A intervalli calcolati aggiorna {@link chevy.model} tramite {@link UpdateManager} e
     * {@link chevy.view} tramite {@link RenderManager}
     */
    private static void run() {
        Log.info("Game loop avviato");
        final int frameDuration = 1000 / TARGET_FRAME_RATE;
        long lastTime = System.currentTimeMillis();
        while (isRunning) {
            final long timeToWait = frameDuration - (System.currentTimeMillis() - lastTime);
            if (timeToWait > 0 && timeToWait <= frameDuration) {
                try {
                    synchronized (mutex) {
                        mutex.wait(timeToWait);
                        while (isPaused) {
                            Log.info("Game loop in pausa");
                            mutex.wait();
                        }
                    }
                } catch (InterruptedException ignored) {}
            }
            final double delta = (System.currentTimeMillis() - lastTime) / 1000d; // secondi
            lastTime = System.currentTimeMillis();

            UpdateManager.update(delta);
            RenderManager.render(delta);
            Toolkit.getDefaultToolkit().sync();
        }
        Log.error("Game loop terminato");
    }
}