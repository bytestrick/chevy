package chevy.control.trapsController;

import chevy.model.entity.collectable.powerUp.HobnailBoots;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.service.Sound;

/**
 * Gestisce le interazioni del giocatore con il pavimento ghiacciato nel gioco.
 */
final class IcyFloorController {
    /**
     * Gestisce l'interazione del giocatore con il pavimento ghiacciato.
     *
     * @param player   il giocatore che interagisce con il pavimento ghiacciato
     */
    static void playerInInteraction(Player player) {
        if (canChangeInGlide(player)) {
            Sound.play(Sound.Effect.SLIDE);
            player.changeState(Player.State.GLIDE);
        }
    }

    static void playerOutInteraction(Player player) {
        if (canChangeInGlide(player)) {
            Sound.play(Sound.Effect.SLIDE);
            player.changeState(Player.State.GLIDE);
        }
    }

    static void update(IcyFloor icyFloor) {
        icyFloor.checkAndChangeState(IcyFloor.State.ICY_FLOOR);
        icyFloor.checkAndChangeState(IcyFloor.State.ICY_FLOOR_SPARKLING);
    }

    private static boolean canChangeInGlide(Player player) {
        HobnailBoots hobnailBoots = (HobnailBoots) player.getOwnedPowerUp(PowerUp.Type.HOBNAIL_BOOTS);
        return hobnailBoots == null || !hobnailBoots.canUse();
    }
}
