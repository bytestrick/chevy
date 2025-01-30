package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;

/**
 * Handles the player interactions with the trapdoor in game.
 */
final class TrapdoorController {
    /**
     * Game room containing the trapdoor to manage
     */
    private final PlayerController playerController;

    TrapdoorController(PlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * Handle the player interaction when on top of the trapdoor.
     */
    void playerInInteraction(Trapdoor trapdoor) {
        if (!trapdoor.isSafeToCross()) {
            playerController.handleInteraction(Interaction.TRAP, trapdoor);
        }
    }

    /**
     * Handle the player interaction when exiting the trapdoor.
     *
     * @param trapdoor the trapdoor the player is exiting
     */
    static void playerOutInteraction(Trapdoor trapdoor) {
        trapdoor.changeState(Trapdoor.State.OPEN);
    }

    static void update(Trapdoor trapdoor) {
        if (trapdoor.checkAndChangeState(Trapdoor.State.DAMAGE)) {
            trapdoor.setSafeToCross(false);
        }
    }
}
