package chevy.service;

import chevy.utils.Log;

import java.awt.Toolkit;

/**
 * Il thread che aggiorna il gioco
 */
public class GameLoop implements Runnable {
    /** FPS del gioco */
    public static final int TARGET_FRAME_RATE = 60;
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
        if (worker == null) {
            worker = Thread.ofPlatform().priority(Thread.MAX_PRIORITY).start(this);
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
        final int frameDuration = 1000 / TARGET_FRAME_RATE;
        long lastTime = System.currentTimeMillis();
        while (isRunning) {
            final long timeToWait = frameDuration - (System.currentTimeMillis() - lastTime);
            if (timeToWait > 0 && timeToWait <= frameDuration) {
                try {
                    synchronized (this) {
                        wait(timeToWait);
                        while (isPaused) {
                            Log.info("Game loop in pausa");
                            wait();
                        }
                    }
                } catch (InterruptedException ignored) {}
            }
            final double delta = (System.currentTimeMillis() - lastTime) / 1000.0d; //
            // conversione in secondi
            lastTime = System.currentTimeMillis();

            UpdateManager.update(delta);
            RenderManager.render(delta);
            Toolkit.getDefaultToolkit().sync();
        }
        worker = null;
    }
}