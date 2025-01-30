package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.entity.staticEntity.environment.traps.Void;

/**
 * Controller to manage the player interactions with the Void trap in game.
 */
final class VoidController {
    /**
     * Controller of the player to manage interactions with the player
     */
    private final PlayerController playerController;

    VoidController(PlayerController playerController) {this.playerController = playerController;}

    /**
     * Manages the player interaction with the Void trap.
     *
     * @param v the Void trap the player is interacting with
     */
    void playerInInteraction(Void v) {
        playerController.handleInteraction(Interaction.TRAP, v);
    }
}
