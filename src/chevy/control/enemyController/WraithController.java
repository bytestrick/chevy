package chevy.control.enemyController;

import chevy.Sound;
import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del Wraith all'interno del gioco. Gestisce come il Wraith risponde agli
 * attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
public class WraithController {
    /**
     * Riferimento alla stanza di gioco in cui si trova il Wraith. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra il Wraith e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          riferimento della stanza di gioco
     * @param playerController riferimento al controllo del giocatore
     */
    public WraithController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni del Wraith con il giocatore.
     *
     * @param player il giocatore che interagisce con il Wraith
     * @param wraith il Wraith che subisce l'interazione
     */
    public void playerInInteraction(Player player, Wraith wraith) {
        switch (player.getCurrentState()) {
            // Se il giocatore è in stato di attacco, il Wraith viene danneggiato in base al danno del giocatore.
            case Player.State.ATTACK -> {
                Sound.getInstance().play(Sound.Effect.GHOST_HIT);
                wraith.setDirection(DirectionsModel.positionToDirection(player, wraith));
                hitWraith(wraith, -1 * player.getDamage());
            }
            default -> Log.warn("Il WraithController non gestisce questa azione: " + player.getCurrentState());
        }
    }

    /**
     * Aggiorna lo stato del Wraith a ogni ciclo di gioco.
     *
     * @param wraith il Wraith da aggiornare
     */
    public void update(Wraith wraith) {
        if (wraith.isDead()) {
            if (wraith.getState(Wraith.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(wraith);
                wraith.removeToUpdate();
                return;
            }
        } else if (wraith.getHealth() <= 0 && wraith.checkAndChangeState(Wraith.State.DEAD)) {
            Sound.getInstance().play(Sound.Effect.GHOST_DEATH);
            wraith.kill();
        }

        if (wraith.canChange(Wraith.State.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(wraith);
            if (direction == null) {
                if (chamber.moveRandomPlus(wraith)) {
                    wraith.changeState(Wraith.State.MOVE);
                }
            } else if (wraith.canAttack() && wraith.getState(Wraith.State.ATTACK).isFinished()) {
                Entity entity = chamber.getNearEntityOnTop(wraith, direction);
                if (entity instanceof Player) {
                    Sound.getInstance().play(Sound.Effect.GHOST_ATTACK);
                    playerController.handleInteraction(InteractionType.ENEMY, wraith);
                    wraith.setCanAttack(false);
                }
            } else if (wraith.canChange(Wraith.State.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(wraith, direction);
                if (entity instanceof Player && wraith.changeState(Wraith.State.ATTACK)) {
                    Sound.getInstance().play(Sound.Effect.GHOST_ATTACK);
                    wraith.setCanAttack(true);
                }
            }
        }
        wraith.checkAndChangeState(Wraith.State.IDLE);
    }

    /**
     * Gestisce le interazioni del Wraith con i proiettili.
     *
     * @param projectile il proiettile che colpisce il Wraith
     * @param wraith     il Wraith che subisce l'interazione
     */
    public void projectileInteraction(Projectile projectile, Wraith wraith) {
        wraith.setDirection(DirectionsModel.positionToDirection(projectile, wraith));
        hitWraith(wraith, -1 * projectile.getDamage());
    }

    /**
     * Applica danno al Wraith e aggiorna il suo stato.
     *
     * @param wraith il Wraith che subisce il danno
     * @param damage il danno da applicare
     */
    private void hitWraith(Wraith wraith, int damage) {
        if (wraith.changeState(Wraith.State.HIT)) {
            wraith.changeHealth(damage);
        }
    }

    public void trapInteraction(Trap trap, Wraith wraith) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> {
                hitWraith(wraith, -1 * trap.getDamage());
            }
            default -> { }
        }
    }
}