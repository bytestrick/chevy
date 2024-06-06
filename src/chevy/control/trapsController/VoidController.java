package chevy.control.trapsController;

import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.staticEntity.environment.traps.Void;

public class VoidController {
    public VoidController() {}


    public void playerInInteraction(Player player, Void v) {
        if (player.changeState(PlayerStates.FALL)) {
            player.changeHealth(-1 * v.getDamage());
        }
    }
}
