package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Beetle extends Enemy {
    private final Vertex idle = new Vertex(State.IDLE, 1.5f);
    private final Vertex move = new Vertex(State.MOVE, 0.5f);
    private final Vertex attack = new Vertex(State.ATTACK, 0.5f);
    private final Vertex hit = new Vertex(State.HIT, 0.15f);
    private final Vertex dead = new Vertex(State.DEAD, 0.3f);

    public Beetle(Vector2<Integer> initPosition) {
        super(initPosition, Type.BEETLE);
        this.health = 10;
        this.currentHealth = health;
        this.maxDamage = 5;
        this.minDamage = 3;
        this.drawLayer = 3;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Beetle");
        stateMachine.setInitialState(idle);

        idle.linkVertex(move);
        idle.linkVertex(attack);
        idle.linkVertex(hit);
        move.linkVertex(idle);
        move.linkVertex(hit);
        attack.linkVertex(idle);
        attack.linkVertex(hit);
        hit.linkVertex(idle);
        hit.linkVertex(dead);
    }

    @Override
    public synchronized Vertex getState(CommonState commonEnumStates) {
        State beetleState = (State) commonEnumStates;
        return switch (beetleState) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
        };
    }

    public enum State implements CommonState {
        MOVE, ATTACK, HIT, DEAD, IDLE
    }
}