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
            default -> {}
        }
    }

    private void projectileInteraction(Projectile projectile, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case Enemy.Type.BAT -> batController.projectileInteraction(projectile, (Bat) enemy);
            case Enemy.Type.SLIME -> slimeController.projectileInteraction(projectile, (Slime) enemy);
            case Enemy.Type.BIG_SLIME -> bigSlimeController.projectileInteraction(projectile, (BigSlime) enemy);
            case Enemy.Type.ZOMBIE -> zombieController.projectileInteraction(projectile, (Zombie) enemy);
            case Enemy.Type.SKELETON -> skeletonController.projectileInteraction(projectile, (Skeleton) enemy);
            case Enemy.Type.FROG -> frogController.projectileInteraction(projectile, (Frog) enemy);
            default -> {}
        }
    }

    private void playerInInteraction(Player player, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case Enemy.Type.BAT -> batController.playerInInteraction(player, (Bat) enemy);
            case Enemy.Type.SLIME -> slimeController.playerInInteraction(player, (Slime) enemy);
            case Enemy.Type.BIG_SLIME -> bigSlimeController.playerInInteraction(player, (BigSlime) enemy);
            case Enemy.Type.ZOMBIE -> zombieController.playerInInteraction(player, (Zombie) enemy);
            case Enemy.Type.SKELETON -> skeletonController.playerInInteraction(player, (Skeleton) enemy);
            case Enemy.Type.FROG -> frogController.playerInInteraction(player, (Frog) enemy);
            default -> {}
        }
    }

    private void updateEnemy(Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case Enemy.Type.BAT -> batController.update((Bat) enemy);
            case Enemy.Type.SLIME -> slimeController.update((Slime) enemy);
            case Enemy.Type.ZOMBIE -> zombieController.update((Zombie) enemy);
            case Enemy.Type.BIG_SLIME -> bigSlimeController.update((BigSlime) enemy);
            case Enemy.Type.SKELETON -> skeletonController.update((Skeleton) enemy);
            case Enemy.Type.FROG -> frogController.update((Frog) enemy);
            default -> {}
        }
    }
}
