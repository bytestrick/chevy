package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.SkeletonStates;

public class SkeletonController {
    private final Chamber chamber;
    private Skeleton skeleton;


    public SkeletonController(Chamber chamber) {
        this.chamber = chamber;
    }

    public void playerInInteraction(PlayerStates action, int value) {
        if (skeleton == null)
            return;
        // ---

        switch (action) {
            case ATTACK -> {
                if (skeleton.isInvincible() && skeleton.changeState(SkeletonStates.INVINCIBILITY)) {
                    skeleton.changeHealth(-1 * value);
                    skeleton.changeState(SkeletonStates.IDLE);
                }
                else {
                    if (skeleton.changeState(SkeletonStates.HIT)) {
                        skeleton.changeHealth(-1 * value);
                    }
                    if (!skeleton.isAlive() && skeleton.changeState(SkeletonStates.DEAD)) {
                        chamber.removeEnemyFormEnemies(skeleton);
                        chamber.removeEntityOnTop(skeleton);
                    }
                    else {
                        skeleton.changeState(SkeletonStates.IDLE);
                    }
                }
            }
            default -> System.out.println("Lo SkeletonController non gestisce questa azione");
        }

        // ---
        skeleton = null;
    }

    public void enemyUpdate() {

    }

    public void setSkeleton(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

}
