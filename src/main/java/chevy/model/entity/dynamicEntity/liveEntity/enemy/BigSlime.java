package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class BigSlime extends Enemy {
    private final Vertex idle = new Vertex(State.IDLE, 0.7f);
    private final Vertex move = new Vertex(State.MOVE, 0.5f);
    private final Vertex attack = new Vertex(State.ATTACK, 0.5f);
    private final Vertex hit = new Vertex(State.HIT, 0.2f);
    private final Vertex dead = new Vertex(State.DEAD, 0.3f);

    public BigSlime(Vector2<Integer> initPosition) {
        super(initPosition, Type.BIG_SLIME);
        this.health = 10;
        this.currentHealth = health;
        this.maxDamage = 3;
        this.minDamage = 2;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Big slime");
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
        State bigSlimeState = (State) commonEnumStates;
        return switch (bigSlimeState) {
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