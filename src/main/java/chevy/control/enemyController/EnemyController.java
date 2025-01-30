package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.Type;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;

/**
 * Manages the behavior and interactions of various types of enemies in the game. Coordinates the specific controllers for each type of enemy ({@link Wraith}, {@link Zombie}, {@link Slime}, {@link BigSlime}, {@link Skeleton}, {@link Beetle}) and manages interactions between player, projectiles, and enemies.
 */
public final class EnemyController {
    private final WraithController wraithController;
    private final ZombieController zombieController;
    private final SlimeController slimeController;
    private final BigSlimeController bigSlimeController;
    private final SkeletonController skeletonController;
    private final BeetleController beetleController;

    /**
     * @param chamber          the game room containing the enemies
     * @param playerController the player controller
     */
    public EnemyController(Chamber chamber, PlayerController playerController) {
        wraithController = new WraithController(chamber, playerController);
        zombieController = new ZombieController(chamber, playerController);
        slimeController = new SlimeController(chamber, playerController);
        bigSlimeController = new BigSlimeController(chamber, playerController);
        skeletonController = new SkeletonController(chamber, playerController);
        beetleController = new BeetleController(chamber, playerController);
    }

    /**
     * Manages the interaction of a trap with an enemy
     *
     * @param trap  the trap that interacts with the enemy
     * @param enemy the enemy that participates in the interaction
     */
    private static void trapInteraction(Trap trap, Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> WraithController.trapInteraction(trap, (Wraith) enemy);
            case Type.SLIME -> SlimeController.trapInteraction(trap, (Slime) enemy);
            case Type.BIG_SLIME -> BigSlimeController.trapInteraction(trap, (BigSlime) enemy);
            case Type.ZOMBIE -> ZombieController.trapInteraction(trap, (Zombie) enemy);
            case Type.SKELETON -> SkeletonController.trapInteraction(trap, (Skeleton) enemy);
            case Type.BEETLE -> BeetleController.trapInteraction(trap, (Beetle) enemy);
            default -> {
            }
        }
    }

    /**
     * Manages the interaction of a projectile with an enemy
     *
     * @param projectile the projectile hitting the enemy
     * @param enemy      the enemy getting hit
     */
    private static void projectileInteraction(Projectile projectile, Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> WraithController.projectileInteraction(projectile, (Wraith) enemy);
            case Type.SLIME -> SlimeController.projectileInteraction(projectile, (Slime) enemy);
            case Type.BIG_SLIME -> BigSlimeController.projectileInteraction(projectile, (BigSlime) enemy);
            case Type.ZOMBIE -> ZombieController.projectileInteraction(projectile, (Zombie) enemy);
            case Type.SKELETON -> SkeletonController.projectileInteraction(projectile, (Skeleton) enemy);
            case Type.BEETLE -> BeetleController.projectileInteraction(projectile, (Beetle) enemy);
            default -> {
            }
        }
    }

    /**
     * Manages the interaction of a player with an enemy
     *
     * @param player the player interacting with the enemy
     * @param enemy  the enemy that participates in the interaction
     */
    private static void playerInInteraction(Player player, Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> WraithController.playerInInteraction(player, (Wraith) enemy);
            case Type.SLIME -> SlimeController.playerInInteraction(player, (Slime) enemy);
            case Type.BIG_SLIME -> BigSlimeController.playerInInteraction(player, (BigSlime) enemy);
            case Type.ZOMBIE -> ZombieController.playerInInteraction(player, (Zombie) enemy);
            case Type.SKELETON -> SkeletonController.playerInInteraction(player, (Skeleton) enemy);
            case Type.BEETLE -> BeetleController.playerInInteraction(player, (Beetle) enemy);
            default -> {
            }
        }
    }

    /**
     * Manages the interaction between dynamic entities
     *
     * @param interaction the type of interaction to manage
     * @param subject     the entity that starts the interaction
     * @param object      the entity that participates in the interaction
     */
    public synchronized void handleInteraction(Interaction interaction, Entity subject,
                                               Enemy object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, object);
            case UPDATE -> updateEnemy((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject, object);
            case TRAP -> trapInteraction((Trap) subject, object);
            default -> {
            }
        }
    }

    /**
     * Update the state of an enemy at each game cycle
     *
     * @param enemy the enemy to update
     */
    private void updateEnemy(Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> wraithController.update((Wraith) enemy);
            case Type.SLIME -> slimeController.update((Slime) enemy);
            case Type.ZOMBIE -> zombieController.update((Zombie) enemy);
            case Type.BIG_SLIME -> bigSlimeController.update((BigSlime) enemy);
            case Type.SKELETON -> skeletonController.update((Skeleton) enemy);
            case Type.BEETLE -> beetleController.update((Beetle) enemy);
            default -> {
            }
        }
    }
}
