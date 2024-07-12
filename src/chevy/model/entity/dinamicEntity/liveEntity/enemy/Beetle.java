package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Beetle extends Enemy {
    public enum EnumState implements CommonEnumStates {
        MOVE,
        ATTACK,
        HIT,
        DEAD,
        IDLE
    }
    private final State idle = new State(EnumState.IDLE, 1.5f);
    private final State move = new State(EnumState.MOVE, 0.5f);
    private final State attack = new State(EnumState.ATTACK, 0.5f);
    private final State hit = new State(EnumState.HIT, 0.15f);
    private final State dead = new State(EnumState.DEAD, 0.3f);


    public Beetle(Vector2<Integer> initPosition) {
        super(initPosition, Type.BEETLE);
        this.health = 10;
        this.maxDamage = 5;
        this.minDamage = 3;
        this.drawLayer = 3;

        initStateMachine();
    }


    private void initStateMachine() {
        stateMachine.setStateMachineName("Beetle");
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
    public synchronized State getState(CommonEnumStates commonEnumStates) {
        EnumState beetleState = (EnumState) commonEnumStates;
        return switch (beetleState) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
        };
    }
}
