package chevy.control.trapsController;

import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.InteractionType;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.staticEntity.environment.traps.Traps;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.List;

public class TrapsUpdateController implements Update {
    private final TrapsController trapsController;
    private final List<Traps> traps;

    public TrapsUpdateController(TrapsController trapsController, List<Traps> traps) {
        this.traps = traps;
        this.trapsController = trapsController;

        UpdateManager.addToUpdate(this);
    }

    @Override
    public void update(double delta) {
        for (Traps trap : traps) {
            trap.incrementNUpdate();
            if (trap.getUpdateEverySecond() * GameSettings.FPS == trap.getCurrentNUpdate()) {
                trap.resetNUpdate();
                trapsController.handleInteraction(InteractionType.UPDATE, trap, null);
            }
        }
    }

    @Override
    public boolean isEnd() {
        return traps.isEmpty();
    }
}
