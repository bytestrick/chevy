package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.service.Sound;

/**
 * Manages the player interactions with the sludge in game.
 */
final class SludgeController {
    private final Chamber chamber;

    /**
     * @param chamber the game room containing the sludge
     */
    SludgeController(Chamber chamber) {
        this.chamber = chamber;
    }

    /**
     * Manages the initial interaction (as soon as it enters the sludge) of the player with the sludge.
     *
     * @param player the player interacting with the sludge
     */
    static void playerInInteraction(Player player) {
        player.changeState(Player.State.SLUDGE);
        Sound.play(Sound.Effect.MUD);
    }

    /**
     * Handles the continuous interaction (when inside the sludge) of the player with the sludge.
     *
     * @param player the player interacting with the sludge
     * @param sludge the sludge that the player is interacting with
     */
    void playerInteraction(Player player, Sludge sludge) {
        if (sludge.getNMoveToUnlock() <= 0) {
            player.changeState(Player.State.IDLE);
            chamber.findAndRemoveEntity(sludge, false);
        } else {
            Sound.play(Sound.Effect.MUD);
            sludge.decreaseNMoveToUnlock();
        }
    }
}
