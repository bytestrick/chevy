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
 * Manage the behavior and interactions of the enemy {@link Wraith} in the game. Manages how it responds to player attacks, to projectile hits, and coordinates its state with its movements.
 */
final class WraithController {
    /**
     * Reference to the game room containing the {@link Wraith}. Used to check positions, add/remove entities, and manage interactions.
     */
    private final Chamber chamber;
    /**
     * Reference to the player controller, used to manage interactions between the {@link Wraith} and the player.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          reference to the game room
     * @param playerController reference to the player controller
     */
    WraithController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Manages interactions between the {@link Wraith} and the player
     *
     * @param player the player interacting with the Wraith
     * @param wraith the Wraith participating in the interaction
     */
    static void playerInInteraction(Player player, Wraith wraith) {
        // If the player is attacking, the Wraith gets damaged in proportion to the player's damage.
        if (player.getState().equals(Player.State.ATTACK)) {
            Sound.play(Sound.Effect.WRAITH_HIT);
            wraith.setDirection(Direction.positionToDirection(player, wraith));
            hitWraith(wraith, -1 * player.getDamage());
        } else {
            Log.warn("WraithController doesn't handle this action: " + player.getState());
        }
    }

    /**
     * Manage the interactions of the {@link Wraith} with the projectiles
     *
     * @param projectile the projectile interacting with the Wraith
     * @param wraith     the Wraith participating in the interaction
     */
    static void projectileInteraction(Projectile projectile, Wraith wraith) {
        wraith.setDirection(Direction.positionToDirection(projectile, wraith));
        hitWraith(wraith, -1 * projectile.getDamage());
    }

    /**
     * Applies damage to the {@link Wraith} and updates its state
     *
     * @param wraith the Wraith to apply the damage to
     * @param damage the amount of damage to apply
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

    /**
     * Update the state of the {@link Wraith} at each game cycle
     *
     * @param wraith the Wraith to update
     */
    void update(Wraith wraith) {
        if (wraith.isDead()) {
            if (wraith.getState(Wraith.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(wraith);
                wraith.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(wraith);
                Data.increment("stats.kills.totalKills.count");
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
                    wraith.setCanAttack(false);
                    wraith.changeState(Wraith.State.MOVE);
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
                    playerController.handleInteraction(Interaction.ENEMY, wraith);
                    wraith.setCanAttack(false);
                }
            }
        }

        if (wraith.checkAndChangeState(Wraith.State.IDLE)) {
            wraith.setCanAttack(false);
        }
    }
}
