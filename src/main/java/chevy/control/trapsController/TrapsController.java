package chevy.control.trapsController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.model.entity.staticEntity.environment.traps.Void;

/**
 * Handles the behavior and interactions of various types of traps in the game. Coordinates the specific controllers for each type of trap and manages interactions with the player.
 */
public final class TrapsController {
    private final SludgeController sludgeController;
    private final VoidController voidController;
    private final TrapdoorController trapdoorController;
    private final SpikedFloorController spikedFloorController;
    private final TotemController totemController;

    /**
     * @param chamber          the game room containing the traps
     * @param playerController the player controller
     */
    public TrapsController(Chamber chamber, PlayerController playerController,
                           EnemyController enemyController) {
        sludgeController = new SludgeController(chamber);
        voidController = new VoidController(playerController);
        trapdoorController = new TrapdoorController(playerController);
        spikedFloorController = new SpikedFloorController(chamber, playerController,
                enemyController);
        totemController = new TotemController(chamber);
    }

    /**
     * Delegates the interaction of the player exiting the trap to the specific controllers.
     *
     * @param player player exiting the trap
     * @param trap   the trap interacting with the player
     */
    private static void playerOutInteraction(Player player, Trap trap) {
        switch (trap.getType()) {
            case Trap.Type.TRAPDOOR -> TrapdoorController.playerOutInteraction((Trapdoor) trap);
            case Trap.Type.ICY_FLOOR -> IcyFloorController.playerOutInteraction(player);
            default -> {}
        }
    }

    /**
     * Handle interactions with traps based on the type of interaction.
     *
     * @param interaction the type of interaction
     * @param subject     the entity that starts the interaction
     * @param trap        the entity that is hit by the interaction
     */
    public synchronized void handleInteraction(Interaction interaction, Entity subject,
                                               Trap trap) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, trap);
            case PLAYER_OUT -> playerOutInteraction((Player) subject, trap);
            case PLAYER -> playerInteraction((Player) subject, trap);
            case UPDATE -> updateTraps((Trap) subject);
        }
    }

    /**
     * Delegates the interaction of the player entering the trap to the specific controllers.
     *
     * @param player player entering the trap
     * @param trap   trap interacting with the player
     */
    private void playerInInteraction(Player player, Trap trap) {
        switch (trap.getType()) {
            case Trap.Type.SLUDGE -> SludgeController.playerInInteraction(player);
            case Trap.Type.ICY_FLOOR -> IcyFloorController.playerInInteraction(player);
            case Trap.Type.VOID -> voidController.playerInInteraction((Void) trap);
            case Trap.Type.TRAPDOOR -> trapdoorController.playerInInteraction((Trapdoor) trap);
            case Trap.Type.SPIKED_FLOOR ->
                    spikedFloorController.playerInInteraction(player, (SpikedFloor) trap);
            default -> {}
        }
    }

    /**
     * Delegates the interaction of the player with the trap to the specific controllers
     *
     * @param player the player interacting with the trap
     * @param trap   the trap interacting with the player
     */
    private void playerInteraction(Player player, Trap trap) {
        if (trap.getType().equals(Trap.Type.SLUDGE)) {
            sludgeController.playerInteraction(player, (Sludge) trap);
        }
    }

    /**
     * Delegates the update of the trap to the specific controllers
     *
     * @param trap the trap to update
     */
    private void updateTraps(Trap trap) {
        switch (trap.getType()) {
            case Trap.Type.SPIKED_FLOOR -> spikedFloorController.update((SpikedFloor) trap);
            case Trap.Type.TOTEM -> totemController.update((Totem) trap);
            case Trap.Type.ICY_FLOOR -> IcyFloorController.update((IcyFloor) trap);
            case Trap.Type.TRAPDOOR -> TrapdoorController.update((Trapdoor) trap);
            default -> {}
        }
    }
}
