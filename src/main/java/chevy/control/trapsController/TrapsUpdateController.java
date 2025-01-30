package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Manages the updates of the traps in the game. Interacts with the updates cycle of the game. Manages the addition, update and removal of traps from the updates.
 */
public final class TrapsUpdateController implements Updatable {
    private final TrapsController trapsController;
    private final Collection<Trap> traps = new ArrayList<>();
    private final List<Trap> trapsToAdd;
    private boolean running;

    /**
     * @param trapsController the controller of the traps responsible for managing the interactions with the traps
     * @param traps           list of traps to add
     */
    public TrapsUpdateController(TrapsController trapsController, List<Trap> traps) {
        this.trapsController = trapsController;
        trapsToAdd = traps;
        running = true;
        UpdateManager.register(this);
    }

    /**
     * Adds new traps to the update list and empties the temporary list.
     */
    private void addTraps() {
        for (Trap trap : trapsToAdd) {
            if (!trap.shouldNotUpdate()) {
                traps.add(trap);
            }
        }
        trapsToAdd.clear();
    }

    /**
     * Updates the state of all the traps on every cycle of the game.
     */
    @Override
    public void update(double delta) {
        addTraps();
        for (Trap trap : traps) {
            trapsController.handleInteraction(Interaction.UPDATE, trap, null);
        }
    }

    public void stopUpdate() {running = false;}

    /**
     * Checks if the updates of the traps are finished.
     *
     * @return {@code true} if there are no traps left to update or the game is not running, {@code false} otherwise
     */
    @Override
    public boolean updateFinished() {return traps.isEmpty() || !running;}
}
