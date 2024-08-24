package chevy.service;

import chevy.settings.GameSettings;
import chevy.utils.Log;

import java.awt.Toolkit;

/**
 * Il thread che aggiorna il gioco.
 */
public class GameLoop implements Runnable {
    private static GameLoop instance;
    private boolean isRunning = false;
    private boolean isPaused = false;

    public static GameLoop getInstance() {
        if (instance == null) {
            instance = new GameLoop();
        }
        return instance;
    }

    public void start() {
        if (isRunning) {
            Log.warn(Thread.currentThread().getName() + ": già in esecuzione");
        } else {
            isRunning = true;
            Thread.ofPlatform().priority(Thread.MAX_PRIORITY).name("Game Loop").start(this);
        }
    }

    public void stop() {
        if (isRunning) {
            synchronized (this) {
                isRunning = false;
                isPaused = false;
                notify();
            }
        }
    }

    public void pause() {
        if (!isPaused) {
            synchronized (this) {
                isPaused = true;
                notify();
            }
            Log.info("Game loop: PAUSA");
        }
    }

    public void resume() {
        if (isPaused) {
            synchronized (this) {
                isPaused = false;
                notify();
            }
            Log.info("Game loop: FINE PAUSA");
        }
    }

    @Override
    public void run() {
        Log.info("Game loop: AVVIATO");
        long lastTime = System.currentTimeMillis();
        while (isRunning) {
            final long timeToWait = GameSettings.FRAME_TARGET_TIME - (System.currentTimeMillis() - lastTime);
            if (timeToWait > 0 && timeToWait <= GameSettings.FRAME_TARGET_TIME) {
                try {
                    synchronized (this) {
                        wait(timeToWait);
                        while (isPaused) {
                            wait();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            final double delta = (System.currentTimeMillis() - lastTime) / 1000.0d; // conversione in secondi
            lastTime = System.currentTimeMillis();

            UpdateManager.update(delta);
            RenderManager.render(delta);
            Toolkit.getDefaultToolkit().sync();
        }
        Log.info("Game loop: TERMINATO");
    }
}