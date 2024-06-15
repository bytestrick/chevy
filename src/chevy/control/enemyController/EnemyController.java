package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;


public class EnemyController {
    private final PlayerController playerController;
    private final BatController batController;
    private final ZombieController zombieController;
    private final SlimeController slimeController;
    private final BigSlimeController bigSlimeController;
    private final SkeletonController skeletonController;
    private final FrogController frogController;
    private final WizardController wizardController;


    public EnemyController(Chamber chamber, PlayerController playerController) {
        this.playerController = playerController;

        this.batController = new BatController(chamber, playerController);
        this.zombieController = new ZombieController(chamber, playerController);
        this.slimeController = new SlimeController(chamber, playerController);
        this.bigSlimeController = new BigSlimeController(chamber, playerController);
        this.skeletonController = new SkeletonController(chamber, playerController);
        this.frogController = new FrogController(chamber, playerController);
        this.wizardController = new WizardController(chamber);
    }


    // subject interagisce con object
    public synchronized void handleInteraction(InteractionTypes interaction, DynamicEntity subject, DynamicEntity object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, (Enemy) object);
            case UPDATE -> updateEnemy((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject, (Enemy) object);
        }
    }

    private void projectileInteraction(Projectile projectile, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> batController.projectileInteraction(projectile, (Bat) enemy);
            case EnemyTypes.SLIME -> slimeController.projectileInteraction(projectile, (Slime) enemy);
            case EnemyTypes.BIG_SLIME -> bigSlimeController.projectileInteraction(projectile, (BigSlime) enemy);
            case EnemyTypes.ZOMBIE -> zombieController.projectileInteraction(projectile, (Zombie) enemy);
            case EnemyTypes.SKELETON -> skeletonController.projectileInteraction(projectile, (Skeleton) enemy);
            case EnemyTypes.FROG -> frogController.projectileInteraction(projectile, (Frog) enemy);
            default -> {}
        }
    }

    private void playerInInteraction(Player player, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> batController.playerInInteraction(player, (Bat) enemy);
            case EnemyTypes.SLIME -> slimeController.playerInInteraction(player, (Slime) enemy);
            case EnemyTypes.BIG_SLIME -> bigSlimeController.playerInInteraction(player, (BigSlime) enemy);
            case EnemyTypes.ZOMBIE -> zombieController.playerInInteraction(player, (Zombie) enemy);
            case EnemyTypes.SKELETON -> skeletonController.playerInInteraction(player, (Skeleton) enemy);
            case EnemyTypes.FROG -> frogController.playerInInteraction(player, (Frog) enemy);
            default -> {}
        }
    }

    private void updateEnemy(Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> batController.update((Bat) enemy);
            case EnemyTypes.SLIME -> slimeController.update((Slime) enemy);
            case EnemyTypes.ZOMBIE -> zombieController.update((Zombie) enemy);
            case EnemyTypes.BIG_SLIME -> bigSlimeController.update((BigSlime) enemy);
            case EnemyTypes.SKELETON -> skeletonController.update((Skeleton) enemy);
            case EnemyTypes.FROG -> frogController.update((Frog) enemy);
            default -> {}
        }
    }
}
