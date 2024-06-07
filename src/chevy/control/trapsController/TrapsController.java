package chevy.control.trapsController;

import chevy.control.enemyController.InteractionType;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DynamicEntity;
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


    public TrapsController(Chamber chamber) {
        this.sludgeController = new SludgeController(chamber);
        this.icyFloorController = new IcyFloorController();
        this.voidController = new VoidController();
        this.trapdoorController = new TrapdoorController(chamber);
        this.spikedFloorController = new SpikedFloorController(chamber);
        this.totemController = new TotemController(chamber);
    }


    public synchronized void handleInteraction(InteractionType interaction, Entity subject, Entity object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, (Traps) object);
            case PLAYER_OUT -> playerOutInteraction((Player) subject, (Traps) object);
            case PLAYER -> playerInteraction((Player) subject, (Traps) object);
            case UPDATE -> updateTraps((Traps) subject);
        }
    }

    private void playerOutInteraction(Player player, Traps traps) {
        switch (traps.getSpecificType()) {
            case TrapsTypes.TRAPDOOR -> trapdoorController.playerOutInteraction((Trapdoor) traps);
            default -> {}
        }
    }

    private void playerInInteraction(Player player, Traps traps) {
        switch (traps.getSpecificType()) {
            case TrapsTypes.SLUDGE -> sludgeController.playerInInteraction(player, (Sludge) traps);
            case TrapsTypes.ICY_FLOOR -> icyFloorController.playerInInteraction(player);
            case TrapsTypes.VOID -> voidController.playerInInteraction(player, (Void) traps);
            case TrapsTypes.TRAPDOOR -> trapdoorController.playerInInteraction(player);
            case TrapsTypes.SPIKED_FLOOR -> spikedFloorController.playerInInteraction(player);
            default -> {}
        }
    }

    private void playerInteraction(Player player, Traps traps) {
        switch (traps.getSpecificType()) {
            case TrapsTypes.SLUDGE -> sludgeController.playerInteraction(player, (Sludge) traps);
            case TrapsTypes.ICY_FLOOR -> icyFloorController.playerInInteraction(player);
            default -> {}
        }
    }

    private void updateTraps(Traps trap) {
        switch (trap.getSpecificType()) {
            case TrapsTypes.SPIKED_FLOOR -> spikedFloorController.update((SpikedFloor) trap);
            case TrapsTypes.TOTEM -> totemController.update((Totem) trap);
            default -> {}
        }
    }
}
