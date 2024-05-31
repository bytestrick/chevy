package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class EnemyController {
    private final Chamber chamber;
    // private final List<Enemy> enemies;
    private final BatController batController;
    private final ZombieController zombieController;
    private SkeletonController skeletonController;
    private SlimeController slimeController;
    private BigSlimeController bigSlimeController;
    private FrogController frogController;
    private WizardController wizardController;


    public EnemyController(Chamber chamber) {
        this.chamber = chamber;
        this.batController = new BatController(chamber);
        this.zombieController = new ZombieController(chamber);
//        this.enemies = chamber.getEnemy();
//
//        for (Enemy enemy : enemies)
//            switch (enemy.getSpecificType()) {
//                case EnemyTypes.BAT -> new BatController(chamber, (Bat) enemy);
//                case EnemyTypes.ZOMBIE -> new ZombieController(chamber, (Zombie) enemy);
//                case EnemyTypes.SKELETON -> new SkeletonController(chamber, (Skeleton) enemy);
//                case EnemyTypes.SLIME -> new SlimeController(chamber, (Slime) enemy);
//                case EnemyTypes.BIG_SLIME -> new BigSlimeController(chamber, (BigSlime) enemy);
//                case EnemyTypes.FROG -> new FrogController(chamber, (Frog) enemy);
//                case EnemyTypes.WIZARD -> new WizardController(chamber, (Wizard) enemy);
//                default -> {}
//            }
    }

    public void playerInteraction(Enemy enemy, PlayerStates action, int value) {
        switch (enemy.getSpecificType()) {
            case EnemyTypes.BAT -> {
                batController.setBat((Bat) enemy);
                batController.playerInteraction(action, value);
            }
            case EnemyTypes.ZOMBIE -> {
                zombieController.setZombie((Zombie) enemy);
                zombieController.playerInteraction(action);
            }
            case EnemyTypes.FROG -> {}
            case EnemyTypes.SLIME -> {}
            case EnemyTypes.WIZARD -> {}
            case EnemyTypes.BIG_SLIME -> {}
            case EnemyTypes.SKELETON -> {}
            default -> {}
        }
    }
}
