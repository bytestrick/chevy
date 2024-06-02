package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.SkeletonStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Skeleton extends Enemy {
    private boolean invincible = true;

    private final State idle = new State(SkeletonStates.IDLE);
    private final State move = new State(SkeletonStates.MOVE);
    private final State attack = new State(SkeletonStates.ATTACK);
    private final State hit = new State(SkeletonStates.HIT);
    private final State dead = new State(SkeletonStates.DEAD);
    private final State invincibility = new State(SkeletonStates.INVINCIBILITY);


    public Skeleton(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.SKELETON);

        this.health = 5;
        this.maxDamage = 4;
        this.minDamage = 1;

        stateMachine.setStateMachineName("Skeleton");
        stateMachine.setInitialState(idle);
        initStateMachine();
    }


    private void initStateMachine() {
        idle.linkState(move);
        idle.linkState(attack);
        idle.linkState(hit);
        idle.linkState(invincibility);
        invincibility.linkState(idle);
        move.linkState(idle);
        attack.linkState(idle);
        hit.linkState(idle);
        hit.linkState(dead);
    }

    public boolean isInvincible() {
        return invincible;
    }

    @Override
    public synchronized void changeHealth(int value) {
        if (invincible) {
            invincible = false;
            System.out.println("Lo SKELETON ha perso la sua invicibilità");
            return;
        }
        super.changeHealth(value);
    }
}
