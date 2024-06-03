package chevy.control.enemyController;

import chevy.control.ChamberController;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;


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
        this.zombieController = new ZombieController(chamber);
        this.slimeController = new SlimeController(chamber, playerController);
        this.bigSlimeController = new BigSlimeController(chamber);
        this.skeletonController = new SkeletonController(chamber);
        this.frogController = new FrogController(chamber);
        this.wizardController = new WizardController(chamber);
    }


    // subject interagisce con object
    public synchronized void handleInteraction(InteractionType interaction, DynamicEntity subject, DynamicEntity object) {
        switch (interaction) {
            case PLAYER -> playerInteraction((Player) subject, (Enemy) object);
            case UPDATE -> updateEnemy((Enemy) subject);
        }
    }

    private void playerInteraction(Player player, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> batController.playerInteraction(player, (Bat) enemy);
            case EnemyTypes.SLIME -> slimeController.playerInteraction(player, (Slime) enemy);
            default -> {}
        }
    }

    private void updateEnemy(Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> batController.update((Bat) enemy);
            case EnemyTypes.SLIME -> slimeController.update((Slime) enemy);
            default -> {}
        }
    }

//    private void playerInteraction(Enemy enemy, PlayerStates playerAction, int value) {
//        switch (enemy.getSpecificType()) {
//            case EnemyTypes.ZOMBIE -> {
//                zombieController.setZombie((Zombie) enemy);
//                zombieController.playerInteraction(playerAction, value);
//            }
//            case EnemyTypes.SKELETON -> {
//                skeletonController.setSkeleton((Skeleton) enemy);
//                skeletonController.playerInteraction(playerAction, value);
//            }
//            case EnemyTypes.WIZARD -> {
//                wizardController.setWizard((Wizard) enemy);
//                wizardController.playerInteraction(playerAction, value);
//            }
//            case EnemyTypes.BIG_SLIME -> {
//                bigSlimeController.setBigSlime((BigSlime) enemy);
//                bigSlimeController.playerInteraction(playerAction, value);
//            }
//            case EnemyTypes.FROG -> {
//                frogController.setFrog((Frog) enemy);
//                frogController.playerInteraction(playerAction, value);
//            }
//            default -> {}
//        }
//    }

//    private void enemyUpdate(Enemy enemy) {
//        switch (enemy.getSpecificType()) {
//            case EnemyTypes.ZOMBIE -> {
//                zombieController.setZombie((Zombie) enemy);
//                zombieController.enemyUpdate();
//            }
//            case EnemyTypes.SKELETON -> {
//                skeletonController.setSkeleton((Skeleton) enemy);
//                skeletonController.enemyUpdate();
//            }
//            case EnemyTypes.SLIME -> {
//                slimeController.setSlime((Slime) enemy);
//                slimeController.enemyUpdate();
//            }
//            case EnemyTypes.WIZARD -> {
//                wizardController.setWizard((Wizard) enemy);
//                wizardController.enemyUpdate();
//            }
//            case EnemyTypes.BIG_SLIME -> {
//                bigSlimeController.setBigSlime((BigSlime) enemy);
//                bigSlimeController.enemyUpdate();
//            }
//            case EnemyTypes.FROG -> {
//                frogController.setFrog((Frog) enemy);
//                frogController.enemyUpdate();
//            }
//            default -> {}
//        }
//    }
}
