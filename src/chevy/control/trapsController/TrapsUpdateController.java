package chevy.control.trapsController;

import chevy.control.InteractionType;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Update;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce gli aggiornamenti delle trappole del gioco. Implementa l'interfaccia Update per integrarsi con il ciclo di
 * aggiornamento del gioco. Gestisce l'aggiunta, l'aggiornamento delle trappole.
 */
public class TrapsUpdateController implements Update {
    private final TrapsController trapsController;
    private final List<Trap> traps = new ArrayList<>();
    private final List<Trap> trapsToAdd;

    /**
     * @param trapsController il controller delle trappole per gestire gli aggiornamenti delle trappole
     * @param traps la lista delle trappole da aggiornare
     */
    public TrapsUpdateController(TrapsController trapsController, List<Trap> traps) {
        this.trapsController = trapsController;
        this.trapsToAdd = traps;

        UpdateManager.addToUpdate(this);
    }

    /**
     * Aggiunge le trappole alla lista degli aggiornamenti se devono essere aggiornate
     * e svuota la lista temporanea.
     */
    private void addTraps() {
        for (Trap trap : trapsToAdd) {
            if (!trap.canRemoveToUpdate()) {
                traps.add(trap);
            }
        }
        trapsToAdd.clear();
    }

    /**
     * Esegue l'aggiornamento delle trappole.
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void update(double delta) {
        addTraps();

        for (Trap trap : traps) {
            trapsController.handleInteraction(InteractionType.UPDATE, trap, null);
        }
    }

    /**
     * Verifica se gli aggiornamenti delle trappole sono terminati.
     * @return true se non ci sono più trappole da aggiornare, altrimenti false
     */
    @Override
    public boolean updateFinished() {
        return traps.isEmpty();
    }
}
