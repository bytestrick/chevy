package chevy.control.collectableController;

import chevy.control.Interaction;
import chevy.model.entity.collectable.Collectable;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Manages the update of the collectables in game and removes of collectables from updating.
 */
public final class CollectableUpdateController implements Updatable {
    private final CollectableController collectableController;
    private final Collection<Collectable> collectables = new ArrayList<>();
    private final List<Collectable> collectablesToAdd;
    private boolean running;

    /**
     * @param collectableController controller of the collectables responsible for managing the interaction
     * @param collectables          collectables to add
     */
    public CollectableUpdateController(CollectableController collectableController,
                                       List<Collectable> collectables) {
        this.collectableController = collectableController;
        collectablesToAdd = collectables;
        running = true;

        // Adds this controller to the update manager.
        UpdateManager.register(this);
    }

    /**
     * Adds new collectables to the update list and empties the temporary list
     */
    private void addCollectables() {
        collectables.addAll(collectablesToAdd);
        collectablesToAdd.clear();
    }

    /**
     * Updates the state of all the collectables on every cycle of the game
     */
    @Override
    public void update(double delta) {
        addCollectables();

        // Iter on the list of collectables to update them and remove the ones that must be removed
        Iterator<Collectable> it = collectables.iterator();
        while (it.hasNext()) {
            Collectable collectable = it.next();
            collectableController.handleInteraction(Interaction.UPDATE, collectable, null);
            if (collectable.shouldNotUpdate()) {
                it.remove();
            }
        }
    }

    public void stopUpdate() {
        running = false;
    }

    @Override
    public boolean updateFinished() {
        return !running;
    }
}
