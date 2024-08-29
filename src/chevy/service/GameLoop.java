package chevy.service;

import chevy.settings.GameSettings;
import chevy.utils.Log;

import java.awt.Toolkit;

/**
 * Il thread che aggiorna il gioco.
 */
public class GameLoop implements Runnable {
    private static GameLoop instance;
    private static boolean isRunning;
    private static Thread worker;
    private static boolean isPaused = false;

    public static GameLoop getInstance() {
        if (instance == null) {
            instance = new GameLoop();
        }
        return instance;
    }

    public synchronized void start() {
        isPaused = false;
        isRunning = true;
        if (worker == null || worker.isInterrupted() || !worker.isAlive()) {
            worker = Thread.ofPlatform().priority(Thread.MAX_PRIORITY).name("Game loop").start(this);
        } else {
            notify();
        }
    }

    public synchronized void stop() {
        if (!isPaused) {
            isPaused = true;
            notify();
        }
    }

    /**
     * A intervalli calcolati aggiorna il model tramite UpdateManager e view tramite RenderManager
     */
    @Override
    public void run() {
        Log.info("Game loop avviato");
        long lastTime = System.currentTimeMillis();
        while (isRunning) {
            final long timeToWait = GameSettings.FRAME_TARGET_TIME - (System.currentTimeMillis() - lastTime);
            if (timeToWait > 0 && timeToWait <= GameSettings.FRAME_TARGET_TIME) {
                try {
                    synchronized (this) {
                        wait(timeToWait);
                        while (isPaused) {
                            Log.info("Game loop in pausa");
                            wait();
                        }
                    }
                } catch (InterruptedException ignored) { }
            }
            final double delta = (System.currentTimeMillis() - lastTime) / 1000.0d; // conversione in secondi
            lastTime = System.currentTimeMillis();

            UpdateManager.update(delta);
            RenderManager.render(delta);
            Toolkit.getDefaultToolkit().sync();
        }
        worker = null;
    }
}