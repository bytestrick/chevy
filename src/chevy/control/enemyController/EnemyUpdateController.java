package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnemyUpdateController implements Update {
    private final EnemyController enemyController;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Enemy> enemiesToAdd;


    public EnemyUpdateController(EnemyController enemyController, List<Enemy> enemies) {
        this.enemyController = enemyController;
        this.enemiesToAdd = enemies;

        UpdateManager.addToUpdate(this);
    }

    private void addEnemies() {
        this.enemies.addAll(enemiesToAdd);
        enemiesToAdd.clear();
    }


    @Override
    public void update(double delta) {
        addEnemies();

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemyController.handleInteraction(InteractionTypes.UPDATE, enemy, null);
            if (enemy.canRemoveToUpdate())
                it.remove();
        }
    }

    @Override
    public boolean updateIsEnd() {
        return enemies.isEmpty();
    }
}
