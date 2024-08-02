package chevy.service;

import chevy.settings.GameSettings;

import java.awt.*;

/**
 * Thread che permette al gioco di aggiornarsi
 */
public class GameLoop implements Runnable {
    private boolean isRunning = false;


    public GameLoop() {
        Thread loop = new Thread(this);
        loop.setPriority(Thread.NORM_PRIORITY);
        this.isRunning = true;
        loop.start();
    }


    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            final long timeToWait = GameSettings.FRAME_TARGET_TIME - (System.currentTimeMillis() - lastTime); // si toglie al timeToWait (durata ideale del frame) il tempo perso a fare l'update e il render

            if (timeToWait > 0 && timeToWait <= GameSettings.FRAME_TARGET_TIME) {
                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            final double delta = (System.currentTimeMillis() - lastTime) / 1000.0d; // conversione in secondi
            lastTime = System.currentTimeMillis();

//             System.out.print("\r delta:  " + delta + "\t");

            UpdateManager.update(delta);
            RenderManager.render(delta);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    /**
     * Termina l'aggiornamento del gioco
     */
    public void stopLoop() { this.isRunning = false; }
}
