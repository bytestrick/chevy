package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class BigSlime extends Enemy {
    private final GlobalState idle = new GlobalState(State.IDLE, 0.7f);
    private final GlobalState move = new GlobalState(State.MOVE, 0.5f);
    private final GlobalState attack = new GlobalState(State.ATTACK, 0.5f);
    private final GlobalState hit = new GlobalState(State.HIT, 0.2f);
    private final GlobalState dead = new GlobalState(State.DEAD, 0.3f);

    public BigSlime(Vector2<Integer> initPosition) {
        super(initPosition, Type.BIG_SLIME);
        this.health = 10;
        this.maxDamage = 3;
        this.minDamage = 2;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Big slime");
        stateMachine.setInitialState(idle);

        idle.linkState(move);
        idle.linkState(attack);
        idle.linkState(hit);
        move.linkState(idle);
        move.linkState(hit);
        attack.linkState(idle);
        attack.linkState(hit);
        hit.linkState(idle);
        hit.linkState(dead);
    }

    @Override
    public synchronized GlobalState getState(CommonState commonEnumStates) {
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