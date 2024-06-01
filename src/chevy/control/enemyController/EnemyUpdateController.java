package chevy.control.enemyController;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;

public class EnemyUpdateController implements Runnable {
    private final int updatePerSecond;
    private final EnemyController enemyController;
    private final Enemy enemy;
    private boolean running = true;


    public EnemyUpdateController(EnemyController enemyController, Enemy enemy) {
        this.enemyController = enemyController;
        this.updatePerSecond = enemy.getUpdatePerSecond();
        this.enemy = enemy;

        Thread th = new Thread(this);
        th.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            enemyController.enemyUpdate(this, enemy);
            try {
                Thread.sleep(updatePerSecond * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
