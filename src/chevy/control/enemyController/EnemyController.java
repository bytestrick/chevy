package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

import java.util.List;

public class EnemyController {
    private final BatController batController;
    private final ZombieController zombieController;
    private final SlimeController slimeController;
    private final BigSlimeController bigSlimeController;
    private final SkeletonController skeletonController;
    private final FrogController frogController;
    private final WizardController wizardController;
    private final List<Enemy> enemies;


    public EnemyController(Chamber chamber) {
        this.batController = new BatController(chamber);
        this.zombieController = new ZombieController(chamber);
        this.slimeController = new SlimeController(chamber);
        this.bigSlimeController = new BigSlimeController(chamber);
        this.skeletonController = new SkeletonController(chamber);
        this.frogController = new FrogController(chamber);
        this.wizardController = new WizardController(chamber);

        this.enemies = chamber.getEnemy();
        for (Enemy enemy : enemies)
            new EnemyUpdateController(this, enemy);
    }


    public void playerInteraction(Enemy enemy, PlayerStates action, int value) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> {
                batController.setBat((Bat) enemy);
                batController.playerInteraction(action, value);
            }
            case EnemyTypes.ZOMBIE -> {
                zombieController.setZombie((Zombie) enemy);
                zombieController.playerInteraction(action, value);
            }
            case EnemyTypes.SKELETON -> {
                skeletonController.setSkeleton((Skeleton) enemy);
                skeletonController.playerInteraction(action, value);
            }
            case EnemyTypes.SLIME -> {
                slimeController.setSlime((Slime) enemy);
                slimeController.playerInteraction(action, value);
            }
            case EnemyTypes.WIZARD -> {
                wizardController.setWizard((Wizard) enemy);
                wizardController.playerInteraction(action, value);
            }
            case EnemyTypes.BIG_SLIME -> {
                bigSlimeController.setBigSlime((BigSlime) enemy);
                bigSlimeController.playerInteraction(action, value);
            }
            case EnemyTypes.FROG -> {
                frogController.setFrog((Frog) enemy);
                frogController.playerInteraction(action, value);
            }
            default -> {}
        }
    }

    public synchronized void enemyUpdate(EnemyUpdateController enemyUpdateController, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> {
                batController.setBat((Bat) enemy);
                batController.enemyUpdate(enemyUpdateController);
            }
            case EnemyTypes.ZOMBIE -> {
                zombieController.setZombie((Zombie) enemy);
                zombieController.enemyUpdate(enemyUpdateController);
            }
            case EnemyTypes.SKELETON -> {
                skeletonController.setSkeleton((Skeleton) enemy);
                skeletonController.enemyUpdate(enemyUpdateController);
            }
            case EnemyTypes.SLIME -> {
                slimeController.setSlime((Slime) enemy);
                slimeController.enemyUpdate(enemyUpdateController);
            }
            case EnemyTypes.WIZARD -> {
                wizardController.setWizard((Wizard) enemy);
                wizardController.enemyUpdate(enemyUpdateController);
            }
            case EnemyTypes.BIG_SLIME -> {
                bigSlimeController.setBigSlime((BigSlime) enemy);
                bigSlimeController.enemyUpdate(enemyUpdateController);
            }
            case EnemyTypes.FROG -> {
                frogController.setFrog((Frog) enemy);
                frogController.enemyUpdate(enemyUpdateController);
            }
            default -> {}
        }
    }
}
