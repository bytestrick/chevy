package chevy.control.enemyController;

import chevy.service.Sound;
import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
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
            case Player.State.ATTACK -> {
                Sound.getInstance().play(Sound.Effect.SLIME_HIT);
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
        if (slime.isDead()) {
            if (slime.getState(Slime.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(slime);
                slime.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(slime);
                return;
            }
        } else if (slime.getCurrentHealth() <= 0 && slime.checkAndChangeState(Slime.State.DEAD)) {
            Sound.getInstance().play(Sound.Effect.SLIME_DEATH);
            slime.kill();
        }

        if (slime.canChange(Slime.State.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
            if (direction == null) {
                if (chamber.moveRandom(slime)) {
                    slime.changeState(Slime.State.MOVE);
                    slime.setCanAttack(false);
                }
            } else if (slime.canChange(Slime.State.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(slime, direction);
                if (entity instanceof Player && slime.changeState(Slime.State.ATTACK)) {
                    Sound.getInstance().play(Sound.Effect.SLIME_HIT);
                    playerController.handleInteraction(InteractionType.ENEMY, slime);
                    slime.setCanAttack(true);
                }
            }
        }

        if (slime.canAttack() && slime.getState(Slime.State.ATTACK).isFinished()) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
            if (direction != null) {
                Entity entity = chamber.getNearEntityOnTop(slime, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(InteractionType.ENEMY, slime);
                    slime.setCanAttack(false);
                }
            }
        }

        if (slime.checkAndChangeState(Slime.State.IDLE)) {
            slime.setCanAttack(false);
        }
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
        if (slime.changeState(Slime.State.HIT)) {
            slime.decreaseHealthShield(damage);
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
