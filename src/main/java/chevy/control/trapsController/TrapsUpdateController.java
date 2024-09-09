package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Gestisce gli aggiornamenti delle trappole del gioco. Implementa l'interfaccia Updatable per
 * integrarsi con il ciclo di
 * aggiornamento del gioco. Gestisce l'aggiunta, l'aggiornamento delle trappole.
 */
public final class TrapsUpdateController implements Updatable {
    private final TrapsController trapsController;
    private final Collection<Trap> traps = new ArrayList<>();
    private final List<Trap> trapsToAdd;
    private boolean updateFinished;

    /**
     * @param trapsController il controller delle trappole per gestire gli aggiornamenti delle
     *                        trappole
     * @param traps           la lista delle trappole da aggiornare
     */
    public TrapsUpdateController(TrapsController trapsController, List<Trap> traps) {
        this.trapsController = trapsController;
        trapsToAdd = traps;
        UpdateManager.register(this);
    }

    /**
     * Aggiunge le trappole alla lista degli aggiornamenti se devono essere aggiornate
     * e svuota la lista temporanea.
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
     * Esegue l'aggiornamento delle trappole.
     */
    @Override
    public void update(double delta) {
        addTraps();
        for (Trap trap : traps) {
            trapsController.handleInteraction(Interaction.UPDATE, trap, null);
        }
    }

    public void stopUpdate() {updateFinished = true;}

    /**
     * Verifica se gli aggiornamenti delle trappole sono terminati.
     *
     * @return {@code true} se non ci sono più trappole da aggiornare, altrimenti {@code false}
     */
    @Override
    public boolean updateFinished() {return traps.isEmpty() || updateFinished;}
}