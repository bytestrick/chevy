package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.dynamicEntity.projectile.SlimeShot;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Manages the behavior and the interactions of an enemy of type {@link Beetle} in game.
 * Manages interactions with the player, with projectiles, updates its state and moves it.
 */
final class BeetleController {
    /**
     * Reference to the game room that contains the Beetle. Used to check positions and add/remove the entity.
     */
    private final Chamber chamber;
    /**
     * References to the game controller, used to interact with the player.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          game room
     * @param playerController the controller of the player
     */
    BeetleController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Manages the interactions between the Beetle and the player.
     *
     * @param player the player interacting with the beetle.
     * @param beetle that beetle that participates in the interaction
     */
    static void playerInInteraction(Player player, Beetle beetle) {
        // If the player has been attacker, the Beetle gets damages in proportion to eh player damage.
        if (player.getState().equals(Player.State.ATTACK)) {
            beetle.setDirection(Direction.positionToDirection(player, beetle));
            hitBeetle(beetle, -1 * player.getDamage());
            Sound.play(Sound.Effect.ROBOTIC_INSECT);
        } else {
            Log.warn("BeetleController doesn't handle this action: " + player.getState());
        }
    }

    /**
     * Manages the interactions between the Beetle and projectiles.
     *
     * @param projectile projectile hitting the Beetle
     * @param beetle     beetle being hit
     */
    static void projectileInteraction(Projectile projectile, Beetle beetle) {
        beetle.setDirection(Direction.positionToDirection(projectile, beetle));
        hitBeetle(beetle, -1 * projectile.getDamage());
        Sound.play(Sound.Effect.ROBOTIC_INSECT);
    }

    /**
     * Manages interactions between Beetle and traps
     *
     * @param trap   traps interacting with the Beetle
     * @param beetle Beetle that receives the interaction
     */
    static void trapInteraction(Trap trap, Beetle beetle) {
        if (trap.getType().equals(Trap.Type.SPIKED_FLOOR)) {
            hitBeetle(beetle, -1 * trap.getDamage());
        }
    }

    /**
     * Applies damage to the Beetle and changes its state to {@link Beetle.State#HIT} if possible
     *
     * @param beetle Beetle being damages
     * @param damage damage to apply
     */
    private static void hitBeetle(Beetle beetle, int damage) {
        if (beetle.changeState(Beetle.State.HIT)) {
            beetle.decreaseHealthShield(damage);
        }
    }

    /**
     * Updates the state of Beetle at every cycle of the game. Manages its change of state, its movement and its actions.
     *
     * @param beetle Beetle to update
     */
    void update(Beetle beetle) {
        // If the Beetle is dead and its "DEAD" state is finished, it is removed from the room.
        if (beetle.isDead()) {
            if (beetle.getState(Beetle.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(beetle);
                beetle.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(beetle);
                Data.increment("stats.kills.totalKills.count");
                Data.increment("stats.kills.enemies.beetle.count");
                return;
            }
        } else if (beetle.getCurrentHealth() <= 0 && beetle.checkAndChangeState(Beetle.State.DEAD)) {
            // If the Beetle is dead, it is removed from the room.
            chamber.spawnSlime(beetle); // power up
            Sound.play(Sound.Effect.BEETLE_DEATH);
            beetle.kill();
        }

        // If the Beetle can change its state to "MOVE", it looks for the player nearby.
        if (beetle.canChange(Beetle.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(beetle, 3);
            // If the player is found, the Beetle starts chasing the player.
            if (direction == null) {
                if (chamber.chase(beetle)) {
                    beetle.setCanAttack(false);
                    beetle.changeState(Beetle.State.MOVE);
                }
            } else if (beetle.canChange(Beetle.State.ATTACK)) {
                // If the player is found, the Beetle starts attacking the player.
                for (int distance = 1; distance <= 3; ++distance) {
                    Entity entity = chamber.getEntityNearOnTop(beetle, direction, distance);
                    // move randomly, don't shoot, if there is an obstacle that cannot be crossed
                    if (!(entity instanceof Player) && !entity.isCrossable()) {
                        if (beetle.checkAndChangeState(Beetle.State.MOVE)) {
                            chamber.moveRandomPlus(beetle);
                        }
                        break;
                    }

                    if (distance > 1 && entity instanceof Player && beetle.changeState(Beetle.State.ATTACK)) {
                        Projectile slimeShot = new SlimeShot(beetle.getPosition(), direction);
                        chamber.addProjectile(slimeShot);
                        chamber.addEntityOnTop(slimeShot);
                        break;
                    } else if (distance == 1 && entity instanceof Player && beetle.changeState(Beetle.State.ATTACK)) {
                        Sound.play(Sound.Effect.BEETLE_ATTACK);
                        beetle.setCanAttack(true);
                    }
                }
            }
        }

        if (beetle.canAttack() && beetle.getState(Beetle.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(beetle);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(beetle, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(Interaction.ENEMY, beetle);
                    beetle.setCanAttack(false);
                }
            }
        }

        if (beetle.checkAndChangeState(Beetle.State.IDLE)) {
            beetle.setCanAttack(false);
        }
    }
}
