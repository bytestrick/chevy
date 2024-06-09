package chevy.control.trapsController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;

public class IcyFloorController {
    private final PlayerController playerController;


    public IcyFloorController(PlayerController playerController) {
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, IcyFloor icyFloor) {
       if (player.changeState(PlayerStates.GLIDE)) {
           playerController.handleInteraction(InteractionType.TRAP, icyFloor);
       }
    }
}
