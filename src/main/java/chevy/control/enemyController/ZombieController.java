package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.Statistics;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del nemico Zombie all'interno del gioco. Gestisce come lo Zombie
 * risponde agli attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
public class ZombieController {
    /**
     * Riferimento alla stanza di gioco in cui si trova lo Zombie. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra lo Zombie e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          riferimento della stanza di gioco
     * @param playerController riferimento al controllo del giocatore
     */
    public ZombieController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni dello Zombie con il giocatore.
     *
     * @param player il giocatore che interagisce con lo Zombie
     * @param zombie lo Zombie che subisce l'interazione
     */
    public void playerInInteraction(Player player, Zombie zombie) {
        switch (player.getCurrentState()) {
            // Se il giocatore è in stato di attacco, lo Zombie viene danneggiato in base al danno del giocatore.
            case Player.State.ATTACK -> {
                Sound.getInstance().play(Sound.Effect.ZOMBIE_HIT);
                zombie.setDirection(DirectionsModel.positionToDirection(player, zombie));
                hitZombie(zombie, -1 * player.getDamage());
            }
            default -> Log.warn("Lo ZombieController non gestisce questa azione: " + player.getCurrentState());
        }
    }

    /**
     * Aggiorna lo stato dello Zombie a ogni ciclo di gioco
     *
     * @param zombie lo Zombie da aggiornare
     */
    public void update(Zombie zombie) {
        if (zombie.isDead()) {
            if (zombie.getState(Zombie.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(zombie);
                zombie.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(zombie);
                Statistics.increase(Statistics.KILLED_ENEMY, 1);
                Statistics.increase(Statistics.KILLED_ZOMBIE, 1);
                return;
            }
        } else if (zombie.getCurrentHealth() <= 0 && zombie.checkAndChangeState(Zombie.State.DEAD)) {
            Sound.getInstance().play(Sound.Effect.ZOMBIE_CHOCKING);
            chamber.spawnSlime(zombie); // power up
            zombie.kill();
        }

        if (zombie.canChange(Zombie.State.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(zombie);
            // Se non c'è un giocatore nelle vicinanze, lo Zombie vaga casualmente.
            if (direction == null) {
                if (chamber.wanderChase(zombie, 4)) {
                    zombie.changeState(Zombie.State.MOVE);
                }
            } else if (zombie.changeState(Zombie.State.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(zombie, direction);
                if (entity instanceof Player) {
                    zombie.setCanAttack(true);
                }
            }
        }

        if (zombie.canAttack() && zombie.getState(Zombie.State.ATTACK).isFinished()) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(zombie);
            if (direction != null) {
                Entity entity = chamber.getNearEntityOnTop(zombie, direction);
                if (entity instanceof Player) {
                    Sound.getInstance().play(Sound.Effect.ZOMBIE_BITE);
                    playerController.handleInteraction(InteractionType.ENEMY, zombie);
                    zombie.setCanAttack(false);
                }
            }
        }

        if (zombie.checkAndChangeState(Zombie.State.IDLE)) {
            zombie.setCanAttack(false);
        }
    }

    /**
     * Gestisce le interazioni dello Zombie con i proiettili.
     *
     * @param projectile il proiettile che colpisce lo Zombie
     * @param zombie     lo Zombie che subisce l'interazione
     */
    public void projectileInteraction(Projectile projectile, Zombie zombie) {
        zombie.setDirection(DirectionsModel.positionToDirection(projectile, zombie));
        hitZombie(zombie, -1 * projectile.getDamage());
    }

    /**
     * Applica danno allo Zombie e aggiorna il suo stato se possibile.
     *
     * @param zombie lo Zombie che subisce il danno
     * @param damage il danno da applicare
     */
    private void hitZombie(Zombie zombie, int damage) {
        if (zombie.changeState(Zombie.State.HIT)) {
            zombie.decreaseHealthShield(damage);
        }
    }

    public void trapInteraction(Trap trap, Zombie zombie) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> {
                hitZombie(zombie, -1 * trap.getDamage());
            }
            default -> { }
        }
    }
}
