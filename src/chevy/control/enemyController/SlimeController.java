package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del nemico Slime all'interno del gioco.
 * Gestisce come lo Slime risponde agli attacchi del giocatore, ai colpi dei proiettili e coordina il suo stato e i
 * suoi movimenti.
 */
public class SlimeController {
    /**
     * Riferimento alla stanza di gioco in cui si trova lo Slime. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra lo Slime e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          riferimento della stanza di gioco
     * @param playerController riferimento al controllo del giocatore
     */
    public SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni dello Slime con il giocatore.
     *
     * @param player il giocatore che interagisce con lo Slime
     * @param slime  lo Slime che subisce l'interazione
     */
    public void playerInInteraction(Player player, Slime slime) {
        switch (player.getCurrentState()) {
            // Se il giocatore è in stato di attacco, lo Slime viene danneggiato in base al danno del giocatore.
            case Player.States.ATTACK -> {
                slime.setDirection(DirectionsModel.positionToDirection(player, slime));
                hitSlime(slime, -1 * player.getDamage());
            }
            default -> Log.warn("Lo slimeController non gestisce questa azione: " + player.getCurrentState());
        }
    }

    /**
     * Aggiorna lo stato dello Slime a ogni ciclo di gioco.
     *
     * @param slime lo Slime da aggiornare.
     */
    public void update(Slime slime) {
        if (!slime.isAlive()) {
            if (slime.getState(Slime.States.DEAD).isFinished()) {
                chamber.removeEntityOnTop(slime);
                slime.removeToUpdate();
                return;
            }
        } else if (slime.getHealth() <= 0 && slime.checkAndChangeState(Slime.States.DEAD)) {
            slime.kill();
        }

        if (slime.canChange(Slime.States.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
            if (direction == null) {
                if (chamber.moveRandom(slime)) {
                    slime.changeState(Slime.States.MOVE);
                }
            } else if (slime.canAttack() && slime.getState(Slime.States.ATTACK).isFinished()) {
                Entity entity = chamber.getNearEntityOnTop(slime, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, slime);
                    slime.setCanAttack(false);
                }
            } else if (slime.canChange(Slime.States.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(slime, direction);
                if (entity instanceof Player && slime.changeState(Slime.States.ATTACK)) {
                    slime.setCanAttack(true);
                }
            }
        }
        slime.checkAndChangeState(Slime.States.IDLE);
    }

    /**
     * Gestisce le interazioni dello Slime con i proiettili.
     *
     * @param projectile il proiettile che colpisce lo Slime
     * @param slime      lo Slime che subisce l'interazione
     */
    public void projectileInteraction(Projectile projectile, Slime slime) {
        slime.setDirection(DirectionsModel.positionToDirection(projectile, slime));
        hitSlime(slime, -1 * projectile.getDamage());
    }

    /**
     * Applica danno allo Slime e aggiorna il suo stato.
     *
     * @param slime  lo Slime che subisce il danno
     * @param damage il danno da applicare
     */
    private void hitSlime(Slime slime, int damage) {
        if (slime.changeState(Slime.States.HIT)) {
            slime.changeHealth(damage);
        }
    }

    public void trapInteraction(Trap trap, Slime slime) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> {
                hitSlime(slime, -1 * trap.getDamage());
            }
            default -> { }
        }
    }
}
