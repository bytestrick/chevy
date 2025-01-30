package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Manages the behavior and interactions of the enemy {@link Skeleton} in the game. Manages how it responds to player attacks, to projectile hits, and coordinates its state with its movements.
 */
final class SkeletonController {
    /**
     * Reference to the game room containing the {@link Skeleton}. Used to check positions, add/remove entities, and manage interactions.
     */
    private final Chamber chamber;
    /**
     * Reference to the player controller, used to manage interactions between the {@link Skeleton} and the player.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          reference to the game room
     * @param playerController reference to the player controller
     */
    SkeletonController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Manages interactions between the {@link Skeleton} and the player
     *
     * @param player   the player interacting with the Skeleton
     * @param skeleton the Skeleton participating in the interaction
     */
    static void playerInInteraction(Player player, Skeleton skeleton) {
        // If the player has been attacker, the Skeleton gets damages in proportion to the player damage.
        if (player.getState().equals(Player.State.ATTACK)) {
            Sound.play(Sound.Effect.SKELETON_HIT);
            hitSkeleton(skeleton, -1 * player.getDamage());
            skeleton.setDirection(Direction.positionToDirection(player, skeleton));
        } else {
            Log.warn("SkeletonController doesn't handle this action: " + player.getState());
        }
    }

    /**
     * Manage interactions between the {@link Skeleton} and projectiles
     *
     * @param projectile the projectile hitting the {@link Skeleton}
     * @param skeleton   the {@link Skeleton} getting hit
     */
    static void projectileInteraction(Projectile projectile, Skeleton skeleton) {
        skeleton.setDirection(Direction.positionToDirection(projectile, skeleton));
        hitSkeleton(skeleton, -1 * projectile.getDamage());
    }

    /**
     * Applies damage to the {@link Skeleton} and updates its state
     *
     * @param skeleton the Skeleton being hit
     * @param damage   the amount of damage to apply
     */
    private static void hitSkeleton(Skeleton skeleton, int damage) {
        if (skeleton.changeState(Skeleton.State.HIT)) {
            skeleton.decreaseHealthShield(damage);
        }
    }

    static void trapInteraction(Trap trap, Skeleton skeleton) {
        if (trap.getType().equals(Trap.Type.SPIKED_FLOOR)) {
            hitSkeleton(skeleton, -1 * trap.getDamage());
        }
    }

    /**
     * Update the state of the {@link Skeleton} at each game cycle
     *
     * @param skeleton the Skeleton to update
     */
    void update(Skeleton skeleton) {
        // death handling
        if (skeleton.isDead()) {
            if (skeleton.getState(Skeleton.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(skeleton);
                skeleton.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(skeleton);
                Data.increment("stats.kills.totalKills.count");
                Data.increment("stats.kills.enemies.skeleton.count");
                return;
            }
        } else if (skeleton.getCurrentHealth() <= 0 && skeleton.checkAndChangeState(Skeleton.State.DEAD)) {
            chamber.spawnSlime(skeleton); // power up
            Sound.play(Sound.Effect.SKELETON_DISASSEMBLED);
            skeleton.kill();
        }

        // movement and attack handling
        if (skeleton.canChange(Skeleton.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(skeleton);
            if (direction == null) {
                if (chamber.chase(skeleton)) {
                    skeleton.setCanAttack(false);
                    skeleton.changeState(Skeleton.State.MOVE);
                }
            } else if (skeleton.canChange(Skeleton.State.ATTACK)) {
                Entity entity = chamber.getEntityNearOnTop(skeleton, direction);
                if (entity instanceof Player && skeleton.changeState(Skeleton.State.ATTACK)) {
                    Sound.play(Sound.Effect.SKELETON_HIT);
                    skeleton.setCanAttack(true);
                }
            }
        }

        if (skeleton.canAttack() && skeleton.getState(Skeleton.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(skeleton);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(skeleton, direction);
                if (entity instanceof Player) {
                    Sound.play(Sound.Effect.SKELETON_HIT);
                    playerController.handleInteraction(Interaction.ENEMY, skeleton);
                    skeleton.setCanAttack(false);
                }
            }
        }

        if (skeleton.checkAndChangeState(Skeleton.State.IDLE)) {
            skeleton.setCanAttack(false);
        }
    }
}
