package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Manages the behavior and the interactions of the enemy {@link BigSlime} in game. Manages how it answers to player attacks, to projectiles and coordinates its state with its movements.
 */
final class BigSlimeController {
    /**
     * Reference to the game room that contains the {@link BigSlime}. Used to check positions, add/remove entities and manage interactions.
     */
    private final Chamber chamber;
    /**
     * Reference to the player controller, used to manage interactions between {@link BigSlime} and the player.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          the game room
     * @param playerController the player controller
     */
    BigSlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Manages interactions between {@link BigSlime} and player
     *
     * @param player   the player interacting with the BigSlime
     * @param bigSlime the BigSlime participating in the interaction
     */
    static void playerInInteraction(Player player, BigSlime bigSlime) {
        // If the player is attacking, the BigSlime gets damaged proportionally to the player attack damage
        if (player.getState() == Player.State.ATTACK) {
            bigSlime.setDirection(Direction.positionToDirection(player, bigSlime));
            hitBigSlime(bigSlime, -1 * player.getDamage());
        } else {
            Log.warn("BigSlimeController doesn't handle this action");
        }
    }

    /**
     * Manages the interactions between {@link BigSlime} and projectiles
     *
     * @param projectile the projectile hitting the {@link BigSlime}
     * @param bigSlime   the {@link BigSlime} getting hit
     */
    static void projectileInteraction(Projectile projectile, BigSlime bigSlime) {
        bigSlime.setDirection(Direction.positionToDirection(projectile, bigSlime));
        hitBigSlime(bigSlime, -1 * projectile.getDamage());
    }

    /**
     * Applies damage to the {@link BigSlime} and updates its state to {@link BigSlime.State#HIT} if possible
     *
     * @param bigSlime the {@link BigSlime} getting hit
     * @param damage   amount of damage to apply
     */
    private static void hitBigSlime(BigSlime bigSlime, int damage) {
        if (bigSlime.changeState(BigSlime.State.HIT)) {
            bigSlime.decreaseHealthShield(damage);
            Sound.play(Sound.Effect.SLIME_HIT);
        }
    }

    static void trapInteraction(Trap trap, BigSlime bigSlime) {
        if (trap.getType().equals(Trap.Type.SPIKED_FLOOR)) {
            hitBigSlime(bigSlime, -1 * trap.getDamage());
        }
    }

    /**
     * Updates the {@link BigSlime}'s state on every cycle of the game. Manages the state change, movement and actions of the {@link BigSlime}.
     *
     * @param bigSlime the {@link BigSlime} to update
     */
    void update(BigSlime bigSlime) {
        // Handling the death of the BigSlime
        if (bigSlime.isDead()) {
            if (bigSlime.getState(BigSlime.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(bigSlime);
                bigSlime.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(bigSlime);
                Data.increment("stats.kills.totalKills.count");
                Data.increment("stats.kills.enemies.bigSlime.count");
                return;
            }
        } else if (bigSlime.getCurrentHealth() <= 0 && bigSlime.checkAndChangeState(BigSlime.State.DEAD)) {
            chamber.spawnSlimeAroundEntity(bigSlime, 2);
            Sound.play(Sound.Effect.SLIME_DEATH);
            bigSlime.kill();
        }

        // Movement and attack
        if (bigSlime.canChange(BigSlime.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(bigSlime);
            if (direction == null) {
                if (chamber.wanderChasePlus(bigSlime, 3)) {
                    bigSlime.setCanAttack(false);
                    bigSlime.changeState(BigSlime.State.MOVE);
                }
            } else if (bigSlime.canChange(BigSlime.State.ATTACK)) {
                Entity entity = chamber.getEntityNearOnTop(bigSlime, direction);
                if (entity instanceof Player && bigSlime.changeState(BigSlime.State.ATTACK)) {
                    Sound.play(Sound.Effect.SLIME_HIT);
                    bigSlime.setCanAttack(true);
                }
            }
        }

        if (bigSlime.canAttack() && bigSlime.getState(BigSlime.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(bigSlime);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(bigSlime, direction);
                if (entity instanceof Player) {
                    Sound.play(Sound.Effect.SLIME_HIT);
                    playerController.handleInteraction(Interaction.ENEMY, bigSlime);
                    bigSlime.setCanAttack(false);
                }
            }
        }

        if (bigSlime.checkAndChangeState(BigSlime.State.IDLE)) {
            bigSlime.setCanAttack(false);
        }
    }
}
