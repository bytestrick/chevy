package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Log;
import chevy.utils.Vector2;

public final class Skeleton extends Enemy {
    private final Vertex idle = new Vertex(State.IDLE, 1.8f);
    private final Vertex move = new Vertex(State.MOVE, 0.5f);
    private final Vertex attack = new Vertex(State.ATTACK, 0.5f);
    private final Vertex hit = new Vertex(State.HIT, 0.15f);
    private final Vertex dead = new Vertex(State.DEAD, 0.3f);
    private final Vertex invincibility = new Vertex(State.INVINCIBILITY);
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

        idle.linkVertex(move);
        idle.linkVertex(attack);
        idle.linkVertex(hit);
        idle.linkVertex(invincibility);
        invincibility.linkVertex(idle);
        move.linkVertex(idle);
        move.linkVertex(hit);
        attack.linkVertex(idle);
        attack.linkVertex(hit);
        hit.linkVertex(idle);
        hit.linkVertex(dead);
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
    public synchronized Vertex getState(CommonState commonStates) {
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