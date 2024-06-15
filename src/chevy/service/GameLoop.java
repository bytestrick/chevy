package chevy.service;

import chevy.settings.GameSettings;

import java.awt.*;

public class GameLoop implements Runnable {
    private boolean isRunning = false;


    public GameLoop() {
        Thread loop = new Thread(this);
        loop.setPriority(Thread.NORM_PRIORITY);
        loop.start();
        this.isRunning = true;
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
            // render
            RenderManager.render(delta);
            Toolkit.getDefaultToolkit().sync();
        }
    }

    public void stopLoop() { this.isRunning = false; }
}
