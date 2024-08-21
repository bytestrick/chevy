package chevy.service;

import chevy.settings.GameSettings;
import chevy.utils.Log;

import java.awt.Toolkit;

/**
 * Il thread che aggiorna il gioco.
 */
public class GameLoop implements Runnable {
    private boolean isRunning;

    public GameLoop() {
        isRunning = true;
        Thread.ofPlatform().priority(Thread.MAX_PRIORITY).name("Game Loop").start(this);
    }

    @Override
    public void run() {
        Log.info(Thread.currentThread().getName() + " avviato");
        long lastTime = System.currentTimeMillis();
        while (isRunning) {
            final long timeToWait = GameSettings.FRAME_TARGET_TIME - (System.currentTimeMillis() - lastTime);
            if (timeToWait > 0 && timeToWait <= GameSettings.FRAME_TARGET_TIME) {
                try {
                    synchronized (this) {
                        wait(timeToWait);
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
        Log.info(Thread.currentThread().getName() + " terminato");
    }

    /**
     * Termina l'aggiornamento del gioco
     */
    public void stopLoop() {
        isRunning = false;
    }
}