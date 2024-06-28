package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utils.Vector2;

public class BigSlime extends Enemy {
    public enum EnumState implements CommonEnumStates {
        MOVE,
        ATTACK,
        HIT,
        DEAD,
        IDLE
    }
    private final State idle = new State(EnumState.IDLE, 0.7f);
    private final State move = new State(EnumState.MOVE, 0.5f);
    private final State attack = new State(EnumState.ATTACK, 0.5f);
    private final State hit = new State(EnumState.HIT, 0.2f);
    private final State dead = new State(EnumState.DEAD, 0.3f);


    public BigSlime(Vector2<Integer> initPosition) {
        super(initPosition, Type.BIG_SLIME);
        this.health = 10;
        this.maxDamage = 3;
        this.minDamage = 2;

        this.updateEverySecond = 1.f;

        stateMachine.setStateMachineName("Big slime");
        stateMachine.setInitialState(idle);
        initStateMachine();
    }


    private void initStateMachine() {
        idle.linkState(move);
        idle.linkState(attack);
        idle.linkState(hit);
        move.linkState(idle);
        attack.linkState(idle);
        hit.linkState(idle);
        hit.linkState(dead);
    }

    @Override
    public State getState(CommonEnumStates commonEnumStates) {
        EnumState bigSlimeState = (EnumState) commonEnumStates;
        return switch (bigSlimeState) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
        };
    }
}
