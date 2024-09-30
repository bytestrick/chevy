package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del nemico {@link Slime} all'interno del gioco.
 * Gestisce come lo Slime risponde agli attacchi del giocatore, ai colpi dei proiettili e
 * coordina il suo stato e i suoi movimenti.
 */
final class SlimeController {
    /**
     * Riferimento alla stanza di gioco in cui si trova lo Slime. Utilizzato per verificare le
     * posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra lo
     * Slime e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          riferimento della stanza di gioco
     * @param playerController riferimento al controllo del giocatore
     */
    SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni dello {@link Slime} con il giocatore
     *
     * @param player il giocatore che interagisce con lo Slime
     * @param slime  lo Slime che subisce l'interazione
     */
    static void playerInInteraction(Player player, Slime slime) {
        // Se il giocatore è in stato di attacco, lo Slime viene danneggiato in base al danno del
        // giocatore.
        if (player.getState().equals(Player.State.ATTACK)) {
            Sound.play(Sound.Effect.SLIME_HIT);
            slime.setDirection(Direction.positionToDirection(player, slime));
            hitSlime(slime, -1 * player.getDamage());
        } else {
            Log.warn("Lo slimeController non gestisce questa azione: " + player.getState());
        }
    }

    /**
     * Gestisce le interazioni dello {@link Slime} con i proiettili.
     *
     * @param projectile il proiettile che colpisce lo Slime
     * @param slime      lo Slime che subisce l'interazione
     */
    static void projectileInteraction(Projectile projectile, Slime slime) {
        slime.setDirection(Direction.positionToDirection(projectile, slime));
        hitSlime(slime, -1 * projectile.getDamage());
    }

    /**
     * Applica danno allo {@link Slime} e aggiorna il suo stato
     *
     * @param slime  lo Slime che subisce il danno
     * @param damage il danno da applicare
     */
    private static void hitSlime(Slime slime, int damage) {
        if (slime.changeState(Slime.State.HIT)) {
            slime.decreaseHealthShield(damage);
        }
    }

    static void trapInteraction(Trap trap, Slime slime) {
        if (trap.getType().equals(Trap.Type.SPIKED_FLOOR)) {
            hitSlime(slime, -1 * trap.getDamage());
        }
    }

    /**
     * Aggiorna lo stato dello {@link Slime} a ogni ciclo di gioco
     *
     * @param slime lo Slime da aggiornare.
     */
    void update(Slime slime) {
        if (slime.isDead()) {
            if (slime.getState(Slime.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(slime);
                slime.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(slime);
                Data.increment("stats.kills.total.count");
                Data.increment("stats.kills.enemies.slime.count");
                return;
            }
        } else if (slime.getCurrentHealth() <= 0 && slime.checkAndChangeState(Slime.State.DEAD)) {
            Sound.play(Sound.Effect.SLIME_DEATH);
            slime.kill();
        }

        if (slime.canChange(Slime.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(slime);
            if (direction == null) {
                if (chamber.moveRandom(slime)) {
                    slime.setCanAttack(false);
                    slime.changeState(Slime.State.MOVE);
                }
            } else if (slime.canChange(Slime.State.ATTACK)) {
                Entity entity = chamber.getEntityNearOnTop(slime, direction);
                if (entity instanceof Player && slime.changeState(Slime.State.ATTACK)) {
                    slime.setCanAttack(true);
                }
            }
        }

        if (slime.canAttack() && slime.getState(Slime.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(slime);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(slime, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(Interaction.ENEMY, slime);
                    slime.setCanAttack(false);
                }
            }
        }

        if (slime.checkAndChangeState(Slime.State.IDLE)) {
            slime.setCanAttack(false);
        }
    }
}