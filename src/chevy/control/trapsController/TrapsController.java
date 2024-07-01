package chevy.control.trapsController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.*;
import chevy.model.entity.staticEntity.environment.traps.Void;

/**
 * La classe TrapdoorController è responsabile della gestione del comportamento e delle interazioni
 * di vari tipi di trappole nel gioco. Coordina i sottocontroller specifici per ogni tipo di trappola e
 * gestisce le interazioni con il giocatore.
 */
public class TrapsController {
    private final SludgeController sludgeController;
    private final IcyFloorController icyFloorController;
    private final VoidController voidController;
    private final TrapdoorController trapdoorController;
    private final SpikedFloorController spikedFloorController;
    private final TotemController totemController;

    /**
     * @param chamber la camera di gioco in cui si trovano le trappole
     * @param playerController il controller del giocatore per gestire le interazioni con il giocatore
     */
    public TrapsController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.sludgeController = new SludgeController(chamber);
        this.icyFloorController = new IcyFloorController(playerController);
        this.voidController = new VoidController(playerController);
        this.trapdoorController = new TrapdoorController(chamber);
        this.spikedFloorController = new SpikedFloorController(chamber, playerController, enemyController);
        this.totemController = new TotemController(chamber);
    }

    /**
     * Gestisce le interazioni con le trappole a seconda del tipo di interazione.
     * @param interaction il tipo di interazione
     * @param subject l'entità che avvia l'interazione
     * @param object l'entità che riceve l'interazione
     */
    public synchronized void handleInteraction(InteractionTypes interaction, Entity subject, Entity object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, (Trap) object);
            case PLAYER_OUT -> playerOutInteraction((Player) subject, (Trap) object);
            case PLAYER -> playerInteraction((Player) subject, (Trap) object);
            case UPDATE -> updateTraps((Trap) subject);
        }
    }

    /**
     * Delega l'interazione del giocatore che esce dalla trappola ai controller specifici.
     * @param player giocatore che esce dalla trappola
     * @param trap la trappola con cui interagisce il giocatore
     */
    private void playerOutInteraction(Player player, Trap trap) {
        switch (trap.getSpecificType()) {
            case Trap.Type.TRAPDOOR -> trapdoorController.playerOutInteraction((Trapdoor) trap);
            case Trap.Type.ICY_FLOOR -> icyFloorController.playerOutInteraction(player, (IcyFloor) trap);
            default -> {}
        }
    }

    /**
     * Delega l'interazione del giocatore che entra nella trappola ai controller specifci.
     * @param player giocatore che entra nella trappola
     * @param trap trappola con cui interagisce il giocatore
     */
    private void playerInInteraction(Player player, Trap trap) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SLUDGE -> sludgeController.playerInInteraction(player, (Sludge) trap);
            case Trap.Type.ICY_FLOOR -> icyFloorController.playerInInteraction(player, (IcyFloor) trap);
            case Trap.Type.VOID -> voidController.playerInInteraction(player, (Void) trap);
            case Trap.Type.TRAPDOOR -> trapdoorController.playerInInteraction(player);
            case Trap.Type.SPIKED_FLOOR -> spikedFloorController.playerInInteraction((SpikedFloor) trap);
            default -> {}
        }
    }

    /**
     * Delega l'interazione del giocatore con la trappola ai controller specifici.
     * @param player il giocatore che interagisce
     * @param trap la trappola con cui interagisce il giocatore
     */
    private void playerInteraction(Player player, Trap trap) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SLUDGE -> sludgeController.playerInteraction(player, (Sludge) trap);
            default -> {}
        }
    }

    /**
     * Delega l'aggiornamento della trappola ai controller specifici.
     * @param trap la trappola da aggiornare
     */
    private void updateTraps(Trap trap) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> spikedFloorController.update((SpikedFloor) trap);
            case Trap.Type.TOTEM -> totemController.update((Totem) trap);
            case Trap.Type.ICY_FLOOR -> icyFloorController.update((IcyFloor) trap);
            default -> {}
        }
    }
}
