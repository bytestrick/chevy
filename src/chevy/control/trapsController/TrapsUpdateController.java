package chevy.control.trapsController;

import chevy.control.InteractionTypes;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.List;

/**
 * Controller che gestisce gli aggiornamenti delle trappole nel gioco.
 */
public class TrapsUpdateController implements Update {
    private final TrapsController trapsController;
    private final List<Trap> traps;

    /**
     * @param trapsController il controller delle trappole per gestire gli aggiornamenti delle trappole
     * @param traps la lista delle trappole da aggiornare
     */
    public TrapsUpdateController(TrapsController trapsController, List<Trap> traps) {
        this.traps = traps;
        this.trapsController = trapsController;

        UpdateManager.addToUpdate(this);
    }

    /**
     * Esegue l'aggiornamento delle trappole.
     * @param delta tempo trascorso dall'ultimo aggiornamento
     */
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

    /**
     * Verifica se gli aggiornamenti delle trappole sono terminati.
     * @return true se non ci sono più trappole da aggiornare, altrimenti false
     */
    @Override
    public boolean updateIsEnd() {
        return traps.isEmpty();
    }
}
