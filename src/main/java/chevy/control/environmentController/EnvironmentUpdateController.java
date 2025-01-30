package chevy.control.environmentController;

import chevy.control.Interaction;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class EnvironmentUpdateController implements Updatable {
    private final EnvironmentController environmentController;
    private final Collection<Environment> environments = new ArrayList<>();
    private final List<Environment> environmentsToAdd;
    private boolean running;

    /**
     * @param environmentController the chests controller
     * @param environments          the chests to add
     */
    public EnvironmentUpdateController(EnvironmentController environmentController, List<Environment> environments) {
        this.environmentController = environmentController;
        environmentsToAdd = environments;
        running = true;
        UpdateManager.register(this);
    }

    /**
     * Adds new chests to the update list and empties the temporary list
     */
    private void addEnvironment() {
        for (Environment environment : environmentsToAdd) {
            if (!environment.shouldNotUpdate()) {
                environments.add(environment);
            }
        }
        environmentsToAdd.clear();
    }

    /**
     * Updates the state of all the chests on every cycle of the game
     */
    @Override
    public void update(double delta) {
        addEnvironment();
        Iterator<Environment> it = environments.iterator();
        while (it.hasNext()) {
            Environment environment = it.next();
            if (environment.shouldNotUpdate()) {
                it.remove();
                return;
            }
            environmentController.handleInteraction(Interaction.UPDATE, environment, null);
        }
    }

    public void stopUpdate() {running = false;}

    /**
     * Checks if the chests updates are finished
     *
     * @return {@code true} if the chests updates are finished, {@code false} otherwise
     */
    @Override
    public boolean updateFinished() {return environments.isEmpty() || !running;}
}
