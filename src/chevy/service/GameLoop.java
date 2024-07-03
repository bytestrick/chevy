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
        Thread loop = new Thread(this);
        loop.setPriority(Thread.NORM_PRIORITY);
        loop.start();
        Log.info("Game loop avviato");
        this.isRunning = true;
    }

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            // Si toglie al timeToWait (durata ideale del frame) il tempo perso a fare l'update e il render.
            final long timeToWait = GameSettings.FRAME_TARGET_TIME - (System.currentTimeMillis() - lastTime);

            if (timeToWait > 0 && timeToWait <= GameSettings.FRAME_TARGET_TIME) {
                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            final double delta = (System.currentTimeMillis() - lastTime) / 1000.0d; // conversione in secondi
            lastTime = System.currentTimeMillis();

            UpdateManager.update(delta);
            // render
            RenderManager.render(delta);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    /**
     * Termina l'aggiornamento del gioco
     */
    public void stopLoop() {
        this.isRunning = false;
        Log.info("Game loop terminato");
    }
}