package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del {@link Wraith} all'interno del gioco.
 * Gestisce come il Wraith risponde agli attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
final class WraithController {
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
    WraithController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni del {@link Wraith} con il giocatore
     *
     * @param player il giocatore che interagisce con il Wraith
     * @param wraith il Wraith che subisce l'interazione
     */
    static void playerInInteraction(Player player, Wraith wraith) {
        // Se il giocatore è in stato di attacco, il Wraith viene danneggiato in base al danno del giocatore.
        if (player.getState().equals(Player.State.ATTACK)) {
            Sound.play(Sound.Effect.WRAITH_HIT);
            wraith.setDirection(Direction.positionToDirection(player, wraith));
            hitWraith(wraith, -1 * player.getDamage());
        } else {
            Log.warn("Il WraithController non gestisce questa azione: " + player.getState());
        }
    }

    /**
     * Aggiorna lo stato del {@link Wraith} a ogni ciclo di gioco
     *
     * @param wraith il Wraith da aggiornare
     */
    void update(Wraith wraith) {
        if (wraith.isDead()) {
            if (wraith.getState(Wraith.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(wraith);
                wraith.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(wraith);
                Data.increment("stats.kills.total.count");
                Data.increment("stats.kills.enemies.wraith.count");
                return;
            }
        } else if (wraith.getCurrentHealth() <= 0 && wraith.checkAndChangeState(Wraith.State.DEAD)) {
            chamber.spawnSlime(wraith); // power up
            Sound.play(Sound.Effect.WRAITH_DEATH);
            wraith.kill();
        }

        if (wraith.canChange(Wraith.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(wraith);
            if (direction == null) {
                if (chamber.moveRandomPlus(wraith)) {
                    wraith.changeState(Wraith.State.MOVE);
                    wraith.setCanAttack(false);
                }
            } else if (wraith.canChange(Wraith.State.ATTACK)) {
                Entity entity = chamber.getEntityNearOnTop(wraith, direction);
                if (entity instanceof Player && wraith.changeState(Wraith.State.ATTACK)) {
                    Sound.play(Sound.Effect.WRAITH_ATTACK);
                    wraith.setCanAttack(true);
                }
            }
        }

        if (wraith.canAttack() && wraith.getState(Wraith.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(wraith);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(wraith, direction);
                if (entity instanceof Player) {
                    Sound.play(Sound.Effect.WRAITH_ATTACK);
                    wraith.setCanAttack(false);
                    playerController.handleInteraction(Interaction.ENEMY, wraith);
                }
            }
        }

        if (wraith.checkAndChangeState(Wraith.State.IDLE)) {
            wraith.setCanAttack(false);
        }
    }

    /**
     * Gestisce le interazioni {@link Wraith} con i proiettili
     *
     * @param projectile il proiettile che colpisce il Wraith
     * @param wraith     il Wraith che subisce l'interazione
     */
    static void projectileInteraction(Projectile projectile, Wraith wraith) {
        wraith.setDirection(Direction.positionToDirection(projectile, wraith));
        hitWraith(wraith, -1 * projectile.getDamage());
    }

    /**
     * Applica danno al {@link Wraith} e aggiorna il suo stato
     *
     * @param wraith il Wraith che subisce il danno
     * @param damage il danno da applicare
     */
    private static void hitWraith(Wraith wraith, int damage) {
        if (wraith.changeState(Wraith.State.HIT)) {
            wraith.decreaseHealthShield(damage);
        }
    }

    static void trapInteraction(Trap trap, Wraith wraith) {
        if (trap.getType().equals(Trap.Type.SPIKED_FLOOR)) {
            hitWraith(wraith, -1 * trap.getDamage());
        }
    }
}