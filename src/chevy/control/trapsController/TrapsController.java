package chevy.control.trapsController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.*;
import chevy.model.entity.staticEntity.environment.traps.Void;

public class TrapsController {
    private final SludgeController sludgeController;
    private final IcyFloorController icyFloorController;
    private final VoidController voidController;
    private final TrapdoorController trapdoorController;
    private final SpikedFloorController spikedFloorController;
    private final TotemController totemController;


    public TrapsController(Chamber chamber, PlayerController playerController) {
        this.sludgeController = new SludgeController(chamber);
        this.icyFloorController = new IcyFloorController(playerController);
        this.voidController = new VoidController(playerController);
        this.trapdoorController = new TrapdoorController(chamber);
        this.spikedFloorController = new SpikedFloorController(chamber);
        this.totemController = new TotemController(chamber);
    }


    public synchronized void handleInteraction(InteractionTypes interaction, Entity subject, Entity object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, (Trap) object);
            case PLAYER_OUT -> playerOutInteraction((Player) subject, (Trap) object);
            case PLAYER -> playerInteraction((Player) subject, (Trap) object);
            case UPDATE -> updateTraps((Trap) subject);
        }
    }

    private void playerOutInteraction(Player player, Trap trap) {
        switch (trap.getSpecificType()) {
            case TrapsTypes.TRAPDOOR -> trapdoorController.playerOutInteraction((Trapdoor) trap);
            default -> {}
        }
    }

    private void playerInInteraction(Player player, Trap trap) {
        switch (trap.getSpecificType()) {
            case TrapsTypes.SLUDGE -> sludgeController.playerInInteraction(player, (Sludge) trap);
            case TrapsTypes.ICY_FLOOR -> icyFloorController.playerInInteraction(player, (IcyFloor) trap);
            case TrapsTypes.VOID -> voidController.playerInInteraction(player, (Void) trap);
            case TrapsTypes.TRAPDOOR -> trapdoorController.playerInInteraction(player);
            case TrapsTypes.SPIKED_FLOOR -> spikedFloorController.playerInInteraction(player);
            default -> {}
        }
    }

    private void playerInteraction(Player player, Trap trap) {
        switch (trap.getSpecificType()) {
            case TrapsTypes.SLUDGE -> sludgeController.playerInteraction(player, (Sludge) trap);
            default -> {}
        }
    }

    private void updateTraps(Trap trap) {
        switch (trap.getSpecificType()) {
            case TrapsTypes.SPIKED_FLOOR -> spikedFloorController.update((SpikedFloor) trap);
            case TrapsTypes.TOTEM -> totemController.update((Totem) trap);
            default -> {}
        }
    }
}
