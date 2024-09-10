package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del nemico {@link Zombie} all'interno del gioco.
 * Gestisce come lo Zombie risponde agli attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
final class ZombieController {
    /**
     * Riferimento alla stanza di gioco in cui si trova lo Zombie. Utilizzato per verificare le
     * posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra lo
     * Zombie e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          riferimento della stanza di gioco
     * @param playerController riferimento al controllo del giocatore
     */
    ZombieController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni dello Zombie con il giocatore.
     *
     * @param player il giocatore che interagisce con lo Zombie
     * @param zombie lo Zombie che subisce l'interazione
     */
    static void playerInInteraction(Player player, Zombie zombie) {
        // Se il giocatore è in stato di attacco, lo Zombie viene danneggiato in base al danno
        // del giocatore.
        if (player.getState().equals(Player.State.ATTACK)) {
            Sound.play(Sound.Effect.ZOMBIE_HIT);
            zombie.setDirection(Direction.positionToDirection(player, zombie));
            hitZombie(zombie, -1 * player.getDamage());
        } else {
            Log.warn("Lo ZombieController non gestisce questa azione: " + player.getState());
        }
    }

    /**
     * Gestisce le interazioni dello {@link Zombie} con i proiettili
     *
     * @param projectile il proiettile che colpisce lo Zombie
     * @param zombie     lo Zombie che subisce l'interazione
     */
    static void projectileInteraction(Projectile projectile, Zombie zombie) {
        zombie.setDirection(Direction.positionToDirection(projectile, zombie));
        hitZombie(zombie, -1 * projectile.getDamage());
    }

    /**
     * Applica danno allo {@link Zombie} e aggiorna il suo stato se possibile
     *
     * @param zombie lo Zombie che subisce il danno
     * @param damage il danno da applicare
     */
    private static void hitZombie(Zombie zombie, int damage) {
        if (zombie.changeState(Zombie.State.HIT)) {
            zombie.decreaseHealthShield(damage);
        }
    }

    static void trapInteraction(Trap trap, Zombie zombie) {
        if (trap.getType().equals(Trap.Type.SPIKED_FLOOR)) {
            hitZombie(zombie, -1 * trap.getDamage());
        }
    }

    /**
     * Aggiorna lo stato dello Zombie a ogni ciclo di gioco
     *
     * @param zombie lo Zombie da aggiornare
     */
    void update(Zombie zombie) {
        if (zombie.isDead()) {
            if (zombie.getState(Zombie.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(zombie);
                zombie.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(zombie);
                Data.increment("stats.kills.total.count");
                Data.increment("stats.kills.enemies.zombie.count");
                return;
            }
        } else if (zombie.getCurrentHealth() <= 0 && zombie.checkAndChangeState(Zombie.State.DEAD)) {
            Sound.play(Sound.Effect.ZOMBIE_CHOCKING);
            chamber.spawnSlime(zombie); // power up
            zombie.kill();
        }

        if (zombie.canChange(Zombie.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(zombie);
            // Se non c'è un giocatore nelle vicinanze, lo Zombie vaga casualmente.
            if (direction == null) {
                if (chamber.wanderChase(zombie, 4)) {
                    zombie.changeState(Zombie.State.MOVE);
                }
            } else if (zombie.changeState(Zombie.State.ATTACK)) {
                Entity entity = chamber.getEntityNearOnTop(zombie, direction);
                if (entity instanceof Player) {
                    zombie.setCanAttack(true);
                }
            }
        }

        if (zombie.canAttack() && zombie.getState(Zombie.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(zombie);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(zombie, direction);
                if (entity instanceof Player) {
                    Sound.play(Sound.Effect.ZOMBIE_BITE);
                    playerController.handleInteraction(Interaction.ENEMY, zombie);
                    zombie.setCanAttack(false);
                }
            }
        }

        if (zombie.checkAndChangeState(Zombie.State.IDLE)) {
            zombie.setCanAttack(false);
        }
    }
}