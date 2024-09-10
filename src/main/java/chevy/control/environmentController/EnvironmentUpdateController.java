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
     * @param environmentController il controller delle casse
     * @param environments          la lista delle casse da aggiornare
     */
    public EnvironmentUpdateController(EnvironmentController environmentController, List<Environment> environments) {
        this.environmentController = environmentController;
        environmentsToAdd = environments;
        running = true;
        UpdateManager.register(this);
    }

    /**
     * Aggiunge le casse alla lista degli aggiornamenti e svuota la lista temporanea
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
     * Esegue l'aggiornamento delle casse
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
     * Verifica se gli aggiornamenti delle casse sono terminati
     *
     * @return {@code true} se non ci sono più trappole da aggiornare, altrimenti {@code false}
     */
    @Override
    public boolean updateFinished() {return environments.isEmpty() || !running;}
}