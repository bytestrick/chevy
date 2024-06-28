package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;

/**
 * Controller per gestire le interazioni del giocatore e delle entità con il pavimento spinato nel gioco.
 */
public class SpikedFloorController {
    private final Chamber chamber;

    /**
     * @param chamber la camera di gioco si trova il pavimento spinato
     */
    public SpikedFloorController(Chamber chamber) {
        this.chamber = chamber;
    }

    /**
     * Gestisce l'interazione del giocatore con il pavimento spinato.
     * @param player il giocatore che interagisce con il pavimento spinato
     */
    public void playerInInteraction(Player player) {
        player.changeState(Player.EnumState.IDLE);
    }

    /**
     * Aggiorna lo stato del pavimento spinato.
     * @param spikedFloor il pavimento spinato da aggiornare
     */
    public void update(SpikedFloor spikedFloor) {
        spikedFloor.toggleStateActive();

        if (spikedFloor.isActive()) {
            Entity entity = chamber.getEntityOnTop(spikedFloor);
            boolean mayBeAttacked = false;

            // Controlla se l'entità sopra il pavimento è un LiveEntity (un entità che ha una vita)
            if (entity instanceof LiveEntity liveEntity) {
                // Se l'entità è un giocatore, cambia il suo stato in HIT se possibile
                if (liveEntity instanceof Player) {
                    if (liveEntity.changeState(Player.EnumState.HIT))
                        mayBeAttacked = true;
                }
                // Se l'entità è un nemico, come un Zombie, cambia il suo stato in HIT se possibile
                else {
                    switch (liveEntity.getSpecificType()) {
                        case Enemy.Type.ZOMBIE -> {
                            if (liveEntity.changeState(Zombie.EnumState.HIT))
                                mayBeAttacked = true;
                        }
                        default -> {}
                    }
                }

                // Se l'entità può essere attaccata, riduce la sua salute e la riporta allo stato precedente
                if (mayBeAttacked) {
                    liveEntity.changeHealth(-1 * spikedFloor.getDamage());
                    liveEntity.changeToPreviousState();
                }
            }
        }
    }
}
