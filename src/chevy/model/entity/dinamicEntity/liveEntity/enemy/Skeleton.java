package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Log;
import chevy.utils.Vector2;

public class Skeleton extends Enemy {
    private final GlobalState idle = new GlobalState(State.IDLE, 1.8f);
    private final GlobalState move = new GlobalState(State.MOVE, 0.5f);
    private final GlobalState attack = new GlobalState(State.ATTACK, 0.5f);
    private final GlobalState hit = new GlobalState(State.HIT, 0.15f);
    private final GlobalState dead = new GlobalState(State.DEAD, 0.3f);
    private final GlobalState invincibility = new GlobalState(State.INVINCIBILITY);
    private boolean invincible = true;

    public Skeleton(Vector2<Integer> initPosition) {
        super(initPosition, Type.SKELETON);

        this.health = 5;
        this.currentHealth = health;
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
    public synchronized void decreaseHealthShield(int value) {
        if (invincible) {
            invincible = false;
            Log.warn("Lo SKELETON ha perso la sua invincibilità");
            return;
        }
        super.decreaseHealthShield(value);
    }

    @Override
    public synchronized GlobalState getState(CommonState commonStates) {
        State skeletonEnum = (State) commonStates;
        return switch (skeletonEnum) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
            case INVINCIBILITY -> invincibility;
        };
    }

    public enum State implements CommonState {
        MOVE, ATTACK, HIT, DEAD, IDLE, INVINCIBILITY
    }
}