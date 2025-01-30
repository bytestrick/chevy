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
 * Manages the behavior and interactions of the enemy {@link Zombie} in the game. Manages how it responds to player attacks, to projectile hits, and coordinates its state with its movements.
 */
final class ZombieController {
    /**
     * Reference to the game room containing the {@link Zombie}. Used to check positions, add/remove entities, and manage interactions.
     */
    private final Chamber chamber;
    /**
     * Reference to the player controller, used to manage interactions between the {@link Zombie} and the player.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          reference to the game room
     * @param playerController reference to the player controller
     */
    ZombieController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Manages interactions between the {@link Zombie} and the player
     *
     * @param player the player interacting with the Zombie
     * @param zombie the Zombie participating in the interaction
     */
    static void playerInInteraction(Player player, Zombie zombie) {
        // If the player is attacking, the Zombie gets damaged proportionally to the player attack damage
        if (player.getState().equals(Player.State.ATTACK)) {
            Sound.play(Sound.Effect.ZOMBIE_HIT);
            zombie.setDirection(Direction.positionToDirection(player, zombie));
            hitZombie(zombie, -1 * player.getDamage());
        } else {
            Log.warn("ZombieController doesn't handle this action: " + player.getState());
        }
    }

    /**
     * Manages the interactions of the {@link Zombie} with the projectiles
     *
     * @param projectile the projectile hitting the Zombie
     * @param zombie     the Zombie getting hit
     */
    static void projectileInteraction(Projectile projectile, Zombie zombie) {
        zombie.setDirection(Direction.positionToDirection(projectile, zombie));
        hitZombie(zombie, -1 * projectile.getDamage());
    }

    /**
     * Applies damage to the {@link Zombie} and updates its state
     *
     * @param zombie the Zombie being hit
     * @param damage the damage to apply
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
     * Updates the state of the {@link Zombie} at each game cycle
     *
     * @param zombie the Zombie to update
     */
    void update(Zombie zombie) {
        if (zombie.isDead()) {
            if (zombie.getState(Zombie.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(zombie);
                zombie.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(zombie);
                Data.increment("stats.kills.totalKills.count");
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
            // If there is no player nearby, the Zombie wanders randomly.
            if (direction == null) { 
                if (chamber.wanderChase(zombie, 4)) {
                    zombie.setCanAttack(false);
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
