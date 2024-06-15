package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Skeleton extends Enemy {
    public enum EnumState implements CommonEnumStates {
        MOVE,
        ATTACK,
        HIT,
        DEAD,
        IDLE,
        INVINCIBILITY
    }
    private boolean invincible = true;
    private final State idle = new State(EnumState.IDLE);
    private final State move = new State(EnumState.MOVE);
    private final State attack = new State(EnumState.ATTACK);
    private final State hit = new State(EnumState.HIT);
    private final State dead = new State(EnumState.DEAD);
    private final State invincibility = new State(EnumState.INVINCIBILITY);


    public Skeleton(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.SKELETON);

        this.health = 5;
        this.maxDamage = 4;
        this.minDamage = 1;

        this.updateEverySecond = 1.f;

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
