package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

public class SkeletonController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public SkeletonController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Skeleton skeleton) {
        switch (player.getCurrentEumState()) {
            case Player.States.ATTACK ->
                    hitSkeleton(skeleton, -1 * player.getDamage());
            default -> System.out.println("Lo SkeletonController non gestisce questa azione");
        }
    }

    public void update(Skeleton skeleton) {
        // attacca se hai il player di fianco
        DirectionsModel direction = chamber.getHitDirectionPlayer(skeleton);
        if (direction != null) {
            if (skeleton.changeState(Skeleton.EnumState.ATTACK)) {
                playerController.handleInteraction(InteractionTypes.ENEMY, skeleton);
            }
        }
        // altrimenti muoviti:
        else if (skeleton.changeState(Skeleton.EnumState.MOVE)) {
           chamber.chase(skeleton);
        }
        skeleton.changeState(Skeleton.EnumState.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Skeleton skeleton) {
        hitSkeleton(skeleton, -1 * projectile.getDamage());
    }

    private void hitSkeleton(Skeleton skeleton, int damage) {
        if (skeleton.changeState(Skeleton.EnumState.HIT))
            skeleton.changeHealth(damage);
        if (!skeleton.isAlive() && skeleton.changeState(Skeleton.EnumState.DEAD)) {
            chamber.removeEnemy(skeleton);
            chamber.removeEntityOnTop(skeleton);
        }
        else
            skeleton.changeState(Skeleton.EnumState.IDLE);
    }
}
