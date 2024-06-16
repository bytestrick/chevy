package chevy.control.trapsController;

import chevy.control.InteractionTypes;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.List;

public class TrapsUpdateController implements Update {
    private final TrapsController trapsController;
    private final List<Trap> traps;

    public TrapsUpdateController(TrapsController trapsController, List<Trap> traps) {
        this.traps = traps;
        this.trapsController = trapsController;

        UpdateManager.addToUpdate(this);
    }

    @Override
    public void update(double delta) {
        for (Trap trap : traps) {
            trap.incrementNUpdate();
            if (trap.getUpdateEverySecond() * GameSettings.FPS == trap.getCurrentNUpdate()) {
                trap.resetNUpdate();
                trapsController.handleInteraction(InteractionTypes.UPDATE, trap, null);
            }
        }
    }

    @Override
    public boolean updateIsEnd() {
        return traps.isEmpty();
    }
}
