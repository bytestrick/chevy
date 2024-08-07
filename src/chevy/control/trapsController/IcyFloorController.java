package chevy.control.trapsController;

import chevy.control.PlayerController;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;

/**
 * Gestisce le interazioni del giocatore con il pavimento ghiacciato nel gioco.
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
     *
     * @param player   il giocatore che interagisce con il pavimento ghiacciato
     * @param icyFloor il pavimento ghiacciato calpestato dal giocatore
     */
    public void playerInInteraction(Player player, IcyFloor icyFloor) {
        player.changeState(Player.State.GLIDE);
    }

    public void playerOutInteraction(Player player, IcyFloor icyFloor) {
        player.changeState(Player.State.GLIDE);
    }

    public void update(IcyFloor icyFloor) {
        icyFloor.checkAndChangeState(IcyFloor.EnumState.ICY_FLOOR);
        icyFloor.checkAndChangeState(IcyFloor.EnumState.ICY_FLOOR_SPARKLING);
    }
}