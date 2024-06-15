package chevy.control.trapsController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.Void;

public class VoidController {
    private final PlayerController playerController;


    public VoidController(PlayerController playerController) {
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Void v) {
        if (player.changeState(Player.States.FALL))
            playerController.handleInteraction(InteractionTypes.TRAP, v);
    }
}
