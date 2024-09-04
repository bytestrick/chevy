package chevy.service;

import chevy.utils.Log;

import java.awt.Toolkit;

/**
 * Il thread che aggiorna il gioco
 */
public class GameLoop {
    /** FPS del gioco */
    public static final int TARGET_FRAME_RATE = 60;
    private static final Thread.Builder.OfPlatform worker =
            Thread.ofPlatform().priority(Thread.MAX_PRIORITY);
    private static final Object mutex = new Object();
    private static boolean isRunning;
    private static boolean isPaused = false;

    public static void start() {
        synchronized (mutex) {
            isPaused = false;
            if (!isRunning) {
                isRunning = true;
                worker.start(GameLoop::run);
            } else {
                mutex.notify();
            }
        }
    }

    public static void stop() {
        synchronized (mutex) {
            if (!isPaused) {
                isPaused = true;
                mutex.notify();
            }
        }
    }

    /**
     * A intervalli calcolati aggiorna il model tramite {@link UpdateManager} e view tramite
     * {@link RenderManager}
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