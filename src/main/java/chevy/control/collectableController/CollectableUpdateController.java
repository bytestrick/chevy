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
 * Gestisce glie aggiornamenti degli oggetti collezionabili nel gioco.
 * Gestisce l'aggiornamento e la rimozione degli aggetti collezionabili dall'aggiornamento.
 */
public final class CollectableUpdateController implements Updatable {
    private final CollectableController collectableController;
    private final Collection<Collectable> collectables = new ArrayList<>();
    private final List<Collectable> collectablesToAdd;
    private boolean updateFinished;

    /**
     * @param collectableController il controller degli oggetti collezionabili responsabile della
     *                              gestione delle
     *                              interazioni.
     * @param collectables          la lista degli oggetti collezionabili da aggiungere.
     */
    public CollectableUpdateController(CollectableController collectableController,
                                       List<Collectable> collectables) {
        this.collectableController = collectableController;
        collectablesToAdd = collectables;

        // Aggiunge questo controller al gestore degli aggiornamenti.
        UpdateManager.register(this);
    }

    /**
     * Aggiunge i nuovi oggetti collezionabili alla lista degli aggiornamenti e svuota la lista
     * temporanea
     */
    private void addCollectables() {
        collectables.addAll(collectablesToAdd);
        collectablesToAdd.clear();
    }

    /**
     * Aggiorna lo stato di tutti gli oggetti collezionabili a ogni ciclo di gioco
     */
    @Override
    public void update(double delta) {
        addCollectables();

        // Itera sulla lista dei collezionabili per aggiornarli e rimuove quelli che devono
        // essere rimossi.
        Iterator<Collectable> it = collectables.iterator();
        while (it.hasNext()) {
            Collectable collectable = it.next();
            collectableController.handleInteraction(Interaction.UPDATE, collectable, null);
            if (collectable.shouldNotUpdate()) {
                it.remove();
            }
        }
    }

    public void stopUpdate() {updateFinished = true;}

    @Override
    public boolean updateFinished() {return updateFinished;}
}