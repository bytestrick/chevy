package chevy.control.trapsController;

import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class IcyFloorController {

    public IcyFloorController() {}


    public void playerInInteraction(Player player) {
       player.changeState(PlayerStates.GLIDE);
    }
}
