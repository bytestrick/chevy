package chevy.control.trapsController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;

/**
 * Controller per gestire le interazioni del giocatore con il pavimento ghiacciato nel gioco.
 */
public class IcyFloorController {
    private final PlayerController playerController;

    /**
     * @param playerController il controller del giocatore usato per gestire l'interazione con il giocatore
     */
    public IcyFloorController(PlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * Gestisce l'interazione del giocatore con il pavimento ghiacciato.
     * @param player il giocatore che interagisce con il pavimento ghiacciato
     * @param icyFloor il pavimento ghiacciato calpestato dal giocatore
     */
    public void playerInInteraction(Player player, IcyFloor icyFloor) {
        if (player.changeState(Player.EnumState.GLIDE)) {
            playerController.handleInteraction(InteractionTypes.TRAP, icyFloor);
        }
    }
}
