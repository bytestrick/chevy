package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Log;
import chevy.utils.Vector2;

public class Skeleton extends Enemy {
    private final State idle = new State(States.IDLE, 1.8f);
    private final State move = new State(States.MOVE, 0.5f);
    private final State attack = new State(States.ATTACK, 0.5f);
    private final State hit = new State(States.HIT, 0.15f);
    private final State dead = new State(States.DEAD, 0.3f);
    private final State invincibility = new State(States.INVINCIBILITY);
    private boolean invincible = true;

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
            Log.warn("Lo SKELETON ha perso la sua invincibilità");
            return;
        }
        super.changeHealth(value);
    }

    @Override
    public synchronized State getState(CommonStates commonEnumStates) {
        States skeletonEnum = (States) commonEnumStates;
        return switch (skeletonEnum) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
            case INVINCIBILITY -> invincibility;
        };
    }

    public enum States implements CommonStates {
        MOVE, ATTACK, HIT, DEAD, IDLE, INVINCIBILITY
    }
}