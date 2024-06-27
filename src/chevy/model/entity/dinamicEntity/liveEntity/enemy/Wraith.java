package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Wraith extends Enemy {
    public enum EnumState implements CommonEnumStates {
        MOVE,
        ATTACK,
        HIT,
        DEAD,
        IDLE
    }
    private final State idle = new State(EnumState.IDLE, 0.8f);
    private final State move = new State(EnumState.MOVE, 0.5f);
    private final State attack = new State(EnumState.ATTACK, 0.5f);
    private final State hit = new State(EnumState.HIT, 0.15f);
    private final State dead = new State(EnumState.DEAD, 0.3f);


    public Wraith(Vector2<Integer> initPosition) {
        super(initPosition, Type.WRAITH);
        this.flying = true;
        this.health = 3;
        this.maxDamage = 2;
        this.minDamage = 1;

        this.updateEverySecond = 0.5f;

        stateMachine.setStateMachineName("Wraith");
        stateMachine.setInitialState(idle);
        initStateMachine();
    }

    private void initStateMachine() {
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
    public State getState(CommonEnumStates commonEnumStates) {
        EnumState wraithState = (EnumState) commonEnumStates;
        return switch (wraithState) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
        };
    }
}
