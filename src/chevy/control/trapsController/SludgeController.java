package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.staticEntity.environment.traps.Sludge;

public class SludgeController {
    private final Chamber chamber;


    public SludgeController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Player player, Sludge sludge) {
        player.changeState(PlayerStates.SLUDGE);
    }

    public void playerInteraction(Player player, Sludge sludge) {
        if (sludge.getNMoveToUnlock() <= 0) {
            player.changeState(PlayerStates.IDLE);
            chamber.removeEntityOnTop(sludge);
            return;
        }

        sludge.decreaseNMoveToUnlock();
    }
}
