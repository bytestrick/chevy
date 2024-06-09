package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Bat;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.SkeletonStates;
import chevy.model.entity.dinamicEntity.stateMachine.ZombieStates;
import chevy.model.pathFinding.AStar;
import chevy.utilz.Utilz;
import chevy.utilz.Vector2;

import java.util.List;
import java.util.Random;

public class SkeletonController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public SkeletonController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Skeleton skeleton) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK ->
                    hitSkeleton(skeleton, -1 * player.getDamage());
            default -> System.out.println("Lo SkeletonController non gestisce questa azione");
        }
    }

    public void update(Skeleton skeleton) {
        // attacca se hai il player di fianco
        DirectionsModel direction = chamber.getHitDirectionPlayer(skeleton);
        if (direction != null) {
            if (skeleton.changeState(SkeletonStates.ATTACK)) {
                playerController.handleInteraction(InteractionType.ENEMY, skeleton);
            }
        }
        // altrimenti muoviti:
        else if (skeleton.changeState(SkeletonStates.MOVE)) {
           chamber.chase(skeleton);
        }
        skeleton.changeState(SkeletonStates.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Skeleton skeleton) {
        hitSkeleton(skeleton, -1 * projectile.getDamage());
    }

    private void hitSkeleton(Skeleton skeleton, int damage) {
        if (skeleton.changeState(SkeletonStates.HIT))
            skeleton.changeHealth(damage);
        if (!skeleton.isAlive() && skeleton.changeState(SkeletonStates.DEAD)) {
            chamber.removeEnemyFormEnemies(skeleton);
            chamber.removeEntityOnTop(skeleton);
        }
        else
            skeleton.changeState(SkeletonStates.IDLE);
    }
}
