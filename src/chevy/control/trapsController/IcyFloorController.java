package chevy.control.trapsController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;

public class IcyFloorController {
    private final PlayerController playerController;


    public IcyFloorController(PlayerController playerController) {
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, IcyFloor icyFloor) {
       if (player.changeState(Player.States.GLIDE)) {
           playerController.handleInteraction(InteractionTypes.TRAP, icyFloor);
       }
    }
}
