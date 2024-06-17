package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.List;

public class EnemyUpdateController implements Update {
    private final EnemyController enemyController;
    private final List<Enemy> enemies;


    public EnemyUpdateController(EnemyController enemyController, List<Enemy> enemies) {
        this.enemyController = enemyController;
        this.enemies = enemies;

        UpdateManager.addToUpdate(this);
    }


    @Override
    public void update(double delta) {
        for (Enemy enemy : enemies) {
            enemy.incrementNUpdate();
            if (enemy.getUpdateEverySecond() * GameSettings.FPS == enemy.getCurrentNUpdate()) {
                enemy.resetNUpdate();
                enemyController.handleInteraction(InteractionType.UPDATE, enemy, null);
            }
        }
    }

    @Override
    public boolean isEnd() {
        return enemies.isEmpty();
    }
}
