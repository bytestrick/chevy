package chevy.control.environmentController;

import chevy.control.InteractionType;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.service.Update;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnvironmentUpdateController implements Update {
    private final EnvironmentController environmentController;
    private final List<Environment> environments = new ArrayList<>();
    private final List<Environment> environmentsToAdd;
    private boolean updateFinished = false;

    /**
     * @param environmentController il controller delle ceste per gestire gli aggiornamenti delle ceste
     * @param environments          la lista delle ceste da aggiornare
     */
    public EnvironmentUpdateController(EnvironmentController environmentController, List<Environment> environments) {
        this.environmentController = environmentController;
        this.environmentsToAdd = environments;

        UpdateManager.addToUpdate(this);
    }

    /**
     * Aggiunge le ceste alla lista degli aggiornamenti
     * e svuota la lista temporanea.
     */
    private void addEnvironment() {
        for (Environment environment : environmentsToAdd) {
            if (!environment.canRemoveToUpdate()) {
                environments.add(environment);
            }
        }
        environmentsToAdd.clear();
    }

    /**
     * Esegue l'aggiornamento delle ceste.
     *
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void update(double delta) {
        addEnvironment();

        Iterator<Environment> it = environments.iterator();
        while (it.hasNext()) {
            Environment environment = it.next();
            if (environment.canRemoveToUpdate()) {
                it.remove();
                return;
            }
            environmentController.handleInteraction(InteractionType.UPDATE, environment, null);
        }
    }

    public void updateTerminate() {
        updateFinished = true;
    }

    /**
     * Verifica se gli aggiornamenti delle ceste sono terminati.
     *
     * @return true se non ci sono più trappole da aggiornare, altrimenti false
     */
    @Override
    public boolean updateFinished() {
        return environments.isEmpty() || updateFinished;
    }
}
