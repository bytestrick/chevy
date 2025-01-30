package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the updates of the enemies in the game. Interacts with the updates cycle of the game. Manages the addition, update and removal of enemies from the updates.
 */
public final class EnemyUpdateController implements Updatable {
    private final EnemyController enemyController;
    private final Collection<Enemy> enemies = new ArrayList<>();
    private final List<Enemy> enemiesToAdd;
    private boolean running, paused;

    /**
     * @param enemyController the controller of the enemy responsible for managing the interactions
     * @param enemies         enemies to add
     */
    public EnemyUpdateController(EnemyController enemyController, List<Enemy> enemies) {
        this.enemyController = enemyController;
        enemiesToAdd = enemies;
        running = true;
        paused = false;

        // Adds this controller to the update manager.
        UpdateManager.register(this);
    }

    public void stopUpdate() {
        running = false;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Adds new enemies to the update list and empties the temporary list.
     */
    private void addEnemies() {
        enemies.addAll(enemiesToAdd);
        enemiesToAdd.clear();
    }

    /**
     * Updates the state of all the enemies on every cycle of the game.
     */
    @Override
    public void update(double delta) {
        if (paused) {
            return;
        }
        addEnemies();

        // Iterates through the list of enemies to update them and remove the ones that must be removed.
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemyController.handleInteraction(Interaction.UPDATE, enemy, null);
            if (enemy.shouldNotUpdate()) {
                it.remove();
            }
        }
    }

    /**
     * Checks if the update is finished, that is, if there are no more enemies to update.
     *
     * @return {@code true} if the enemies list is empty or the update is not running
     */
    @Override
    public boolean updateFinished() {
        return enemies.isEmpty() || !running;
    }
}
