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
 * Manages the behavior and interactions of the enemy {@link Slime} in the game. Manages how it responds to player attacks, to projectile hits, and coordinates its state with its movements.
 */
final class SlimeController {
    /**
     * Reference to the game room containing the {@link Slime}. Used to check positions, add/remove entities, and manage interactions.
     */
    private final Chamber chamber;
    /**
     * Reference to the player controller, used to manage interactions between the {@link Slime} and the player.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          reference to the game room
     * @param playerController reference to the player controller
     */
    SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Manages interactions between the {@link Slime} and the player
     *
     * @param player the player interacting with the Slime
     * @param slime  the Slim participating in the interaction
     */
    static void playerInInteraction(Player player, Slime slime) {
        // If the player is attacking, the Slime gets damaged in proportion to the player's damage.
        if (player.getState().equals(Player.State.ATTACK)) {
            Sound.play(Sound.Effect.SLIME_HIT);
            slime.setDirection(Direction.positionToDirection(player, slime));
            hitSlime(slime, -1 * player.getDamage());
        } else {
            Log.warn("SlimeController doesn't handle this action: " + player.getState());
        }
    }

    /**
     * Manages the interactions between the {@link Slime} and the projectiles.
     *
     * @param projectile the projectile hitting the {@link Slime}
     * @param slime      the Slime getting hit
     */
    static void projectileInteraction(Projectile projectile, Slime slime) {
        slime.setDirection(Direction.positionToDirection(projectile, slime));
        hitSlime(slime, -1 * projectile.getDamage());
    }

    /**
     * Applies damage to the {@link Slime} and updates its state
     *
     * @param slime  the Slime being hit
     * @param damage the amount of damage to apply
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
     * Update the state of the {@link Slime} at each game cycle
     *
     * @param slime the slime to update
     */
    void update(Slime slime) {
        if (slime.isDead()) {
            if (slime.getState(Slime.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(slime);
                slime.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(slime);
                Data.increment("stats.kills.totalKills.count");
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
