package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

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
    private final State idle = new State(EnumState.IDLE, 1.8f);
    private final State move = new State(EnumState.MOVE, 0.5f);
    private final State attack = new State(EnumState.ATTACK, 0.5f);
    private final State hit = new State(EnumState.HIT, 0.15f);
    private final State dead = new State(EnumState.DEAD, 0.3f);
    private final State invincibility = new State(EnumState.INVINCIBILITY);


    public Skeleton(Vector2<Integer> initPosition) {
        super(initPosition, Type.SKELETON);

        this.health = 5;
        this.maxDamage = 4;
        this.minDamage = 1;

        initStateMachine();
    }


    private void initStateMachine() {
        stateMachine.setStateMachineName("Skeleton");
        stateMachine.setInitialState(idle);

        idle.linkState(move);
        idle.linkState(attack);
        idle.linkState(hit);
        idle.linkState(invincibility);
        invincibility.linkState(idle);
        move.linkState(idle);
        move.linkState(hit);
        attack.linkState(idle);
        attack.linkState(hit);
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
            System.out.println("[-] Lo SKELETON ha perso la sua invicibilità");
            return;
        }
        super.changeHealth(value);
    }

    @Override
    public synchronized State getState(CommonEnumStates commonEnumStates) {
        EnumState skeletonEnuk = (EnumState) commonEnumStates;
        return switch (skeletonEnuk) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
            case INVINCIBILITY -> invincibility;
        };
    }
}
