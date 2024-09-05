package chevy.control.collectableController;

import chevy.control.Interaction;
import chevy.model.entity.collectable.Collectable;
import chevy.service.Update;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * La classe CollectableUpdateController è responsabile della gestione degli aggiornamenti
 * degli oggetti collezionabili nel gioco. Implementa l'interfaccia Update per integrarsi con il ciclo di
 * aggiornamento del gioco. Gestisce l'aggiornamento e la rimozione degli aggetti collezionabili
 * dall'aggiornamento.
 */
public final class CollectableUpdateController implements Update {
    private final CollectableController collectableController;
    private final List<Collectable> collectables = new ArrayList<>();
    private final List<Collectable> collectablesToAdd;
    private boolean updateFinished = false;

    /**
     * @param collectableController il controller degli oggetti collezionabili responsabile della gestione delle
     *                              interazioni.
     * @param collectables          la lista degli oggetti collezionabili da aggiungere.
     */
    public CollectableUpdateController(CollectableController collectableController, List<Collectable> collectables) {
        this.collectableController = collectableController;
        this.collectablesToAdd = collectables;

        // Aggiunge questo controller al gestore degli aggiornamenti.
        UpdateManager.addToUpdate(this);
    }

    /**
     * Aggiunge i nuovi oggetti collezionabili alla lista degli aggiornamenti e svuota la lista temporanea.
     */
    private void addCollectables() {
        this.collectables.addAll(collectablesToAdd);
        collectablesToAdd.clear();
    }

    /**
     * Aggiorna lo stato di tutti gli oggetti collezionabili a ogni ciclo di gioco.
     *
     * @param delta il tempo trascorso dall'ultimo aggiornamento.
     */
    @Override
    public void update(double delta) {
        addCollectables();

        // Itera sulla lista dei collezionabili per aggiornarli e rimuove quelli che devono essere rimossi.
        Iterator<Collectable> it = collectables.iterator();
        while (it.hasNext()) {
            Collectable collectable = it.next();
            collectableController.handleInteraction(Interaction.UPDATE, collectable, null);
            if (collectable.canRemoveToUpdate()) {
                it.remove();
            }
        }
    }

    public void updateTerminate() {
        updateFinished = true;
    }

    @Override
    public boolean updateFinished() {
        return updateFinished;
    }
}