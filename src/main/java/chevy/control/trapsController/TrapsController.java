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
 * Gestisce il comportamento e le interazioni di vari tipi di trappole nel gioco. Coordina i
 * sotto controller
 * specifici per ogni tipo di trappola e
 * gestisce le interazioni con il giocatore.
 */
public final class TrapsController {
    private final SludgeController sludgeController;
    private final VoidController voidController;
    private final TrapdoorController trapdoorController;
    private final SpikedFloorController spikedFloorController;
    private final TotemController totemController;

    /**
     * @param chamber          la camera di gioco in cui si trovano le trappole
     * @param playerController il controller del giocatore per gestire le interazioni con il
     *                         giocatore
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
     * Delega l'interazione del giocatore che esce dalla trappola ai controller specifici.
     *
     * @param player giocatore che esce dalla trappola
     * @param trap   la trappola con cui interagisce il giocatore
     */
    private static void playerOutInteraction(Player player, Trap trap) {
        switch (trap.getType()) {
            case Trap.Type.TRAPDOOR -> TrapdoorController.playerOutInteraction((Trapdoor) trap);
            case Trap.Type.ICY_FLOOR -> IcyFloorController.playerOutInteraction(player);
            default -> {}
        }
    }

    /**
     * Gestisce le interazioni con le trappole a seconda del tipo di interazione.
     *
     * @param interaction il tipo di interazione
     * @param subject     l'entità che avvia l'interazione
     * @param trap        l'entità che riceve l'interazione
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
     * Delega l'interazione del giocatore che entra nella trappola ai controller specifici.
     *
     * @param player giocatore che entra nella trappola
     * @param trap   trappola con cui interagisce il giocatore
     */
    private void playerInInteraction(Player player, Trap trap) {
        switch (trap.getType()) {
            case Trap.Type.SLUDGE -> SludgeController.playerInInteraction(player);
            case Trap.Type.ICY_FLOOR -> IcyFloorController.playerInInteraction(player);
            case Trap.Type.VOID -> voidController.playerInInteraction(player, (Void) trap);
            case Trap.Type.TRAPDOOR -> trapdoorController.playerInInteraction((Trapdoor) trap);
            case Trap.Type.SPIKED_FLOOR ->
                    spikedFloorController.playerInInteraction(player, (SpikedFloor) trap);
            default -> {}
        }
    }

    /**
     * Delega l'interazione del giocatore con la trappola ai controller specifici
     *
     * @param player il giocatore che interagisce
     * @param trap   la trappola con cui interagisce il giocatore
     */
    private void playerInteraction(Player player, Trap trap) {
        if (trap.getType().equals(Trap.Type.SLUDGE)) {
            sludgeController.playerInteraction(player, (Sludge) trap);
        }
    }

    /**
     * Delega l'aggiornamento della trappola ai controller specifici
     *
     * @param trap la trappola da aggiornare
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