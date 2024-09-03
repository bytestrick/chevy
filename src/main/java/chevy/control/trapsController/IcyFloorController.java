package chevy.control.trapsController;

import chevy.control.PlayerController;
import chevy.model.entity.collectable.powerUp.HobnailBoots;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.service.Sound;

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
        if (canChangeInGlide(player)) {
            Sound.play(Sound.Effect.SLIDE);
            player.changeState(Player.State.GLIDE);
        }
    }

    public void playerOutInteraction(Player player, IcyFloor icyFloor) {
        if (canChangeInGlide(player)) {
            Sound.play(Sound.Effect.SLIDE);
            player.changeState(Player.State.GLIDE);
        }
    }

    public void update(IcyFloor icyFloor) {
        icyFloor.checkAndChangeState(IcyFloor.EnumState.ICY_FLOOR);
        icyFloor.checkAndChangeState(IcyFloor.EnumState.ICY_FLOOR_SPARKLING);
    }

    private boolean canChangeInGlide(Player player) {
        HobnailBoots hobnailBoots = (HobnailBoots) player.getOwnedPowerUp(PowerUp.Type.HOBNAIL_BOOTS);
        return hobnailBoots == null || !hobnailBoots.canUse();
    }
}
