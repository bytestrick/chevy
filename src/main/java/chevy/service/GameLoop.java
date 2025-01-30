package chevy.service;

import chevy.utils.Log;

import java.awt.*;

/**
 * The thread that updates the game
 */
public final class GameLoop {
    /**
     * The optimal FPS of the game
     */
    private static final int TARGET_FRAME_RATE = 60;
    private static final Thread.Builder.OfPlatform worker =
            Thread.ofPlatform().priority(Thread.MAX_PRIORITY);
    private static final Object mutex = new Object();
    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static boolean isRunning, isPaused;
    /**
     * It's used to keep track of the time played
     */
    private static long timeStarted;

    /**
     * The first time creates the {@link #worker} and starts it. All the subsequent calls stop the pause.
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
     * Puts the {@link #worker} in waiting state
     */
    public static void stop() {
        synchronized (mutex) {
            if (!isPaused) {
                isPaused = true;
                mutex.notify();
                // narrowing conversion
                final int value = Long.valueOf(System.currentTimeMillis() - timeStarted).intValue();
                assert value > 0 : "negative played time: " + value;
                Data.increase("stats.time.totalPlayed.count", value);
            }
        }
    }

    /**
     * At regular intervals updates {@link chevy.model} through {@link UpdateManager} and {@link chevy.view} through {@link RenderManager}
     */
    private static void run() {
        Log.info("Game loop started");
        final int frameDuration = 1000 / TARGET_FRAME_RATE;
        long lastTime = System.currentTimeMillis();
        while (isRunning) {
            final long timeToWait = frameDuration - (System.currentTimeMillis() - lastTime);
            if (timeToWait > 0 && timeToWait <= frameDuration) {
                try {
                    synchronized (mutex) {
                        mutex.wait(timeToWait);
                        while (isPaused) {
                            Log.info("Game loop paused");
                            mutex.wait();

                            // After the duration of the indefinite pause, the old value of lastTime has no meaning
                            lastTime = System.currentTimeMillis();
                        }
                    }
                } catch (InterruptedException ignored) {
                }
            }
            final double delta = (System.currentTimeMillis() - lastTime) / 1000d; // seconds
            lastTime = System.currentTimeMillis();

            UpdateManager.update(delta);
            RenderManager.render(delta);
            toolkit.sync();
        }
        Log.error("Game loop stopped");
    }
}
