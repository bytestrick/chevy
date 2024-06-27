package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
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
            case Player.EnumState.ATTACK -> {
                hitSkeleton(skeleton, -1 * player.getDamage());
                skeleton.setDirection(DirectionsModel.positionToDirection(player, skeleton));
            }
            default -> System.out.println("[!] Lo SkeletonController non gestisce questa azione: " + player.getCurrentEumState());
        }
    }

    public void update(Skeleton skeleton) {
        if (!skeleton.isAlive()) {
            if (skeleton.getState(Skeleton.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(skeleton);
                skeleton.removeToUpdate();
                return;
            }
        }
        else if (skeleton.getHealth() <= 0 && skeleton.checkAndChangeState(Skeleton.EnumState.DEAD)) {
            skeleton.kill();
        }

        if (skeleton.canChange(Skeleton.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(skeleton);
            if (direction == null) {
                if (chamber.chase(skeleton)) {
                    skeleton.changeState(Skeleton.EnumState.MOVE);
                }
            }
            else if (skeleton.canChange(Skeleton.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(skeleton, direction);
                if (entity instanceof Player && skeleton.changeState(Skeleton.EnumState.ATTACK)) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, skeleton);
                }
            }
        }
        skeleton.checkAndChangeState(Skeleton.EnumState.IDLE);

//        // attacca se hai il player di fianco
//        DirectionsModel direction = chamber.getHitDirectionPlayer(skeleton);
//        if (direction != null) {
//            if (skeleton.changeState(Skeleton.EnumState.ATTACK)) {
//                playerController.handleInteraction(InteractionTypes.ENEMY, skeleton);
//            }
//        }
//        // altrimenti muoviti:
//        else if (skeleton.changeState(Skeleton.EnumState.MOVE)) {
//           chamber.chase(skeleton);
//        }
//        skeleton.changeState(Skeleton.EnumState.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Skeleton skeleton) {
        skeleton.setDirection(DirectionsModel.positionToDirection(projectile, skeleton));
        hitSkeleton(skeleton, -1 * projectile.getDamage());
    }

    private void hitSkeleton(Skeleton skeleton, int damage) {
        if (skeleton.changeState(Skeleton.EnumState.HIT))
            skeleton.changeHealth(damage);
    }
}
