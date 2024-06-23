package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Slime extends Enemy {
    public enum EnumState implements CommonEnumStates {
        MOVE,
        ATTACK,
        HIT,
        DEAD,
        IDLE
    }
    private final State idle = new State(EnumState.IDLE, 1.f, true);
    private final State move = new State(EnumState.MOVE, 0.5f);
    private final State attack = new State(EnumState.ATTACK, 0.5f);
    private final State hit = new State(EnumState.HIT, 0.2f);
    private final State dead = new State(EnumState.DEAD, 0.15f);


    public Slime(Vector2<Integer> initPosition) {
        super(initPosition, Type.SLIME);
        this.health = 3;
        this.maxDamage = 2;
        this.minDamage = 1;

        this.updateEverySecond = 1.f;

        this.stateMachine.setStateMachineName("Slime");
        this.stateMachine.setInitialState(idle);
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
        EnumState slimeState = (EnumState) commonEnumStates;
        return switch (slimeState) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
        };
    }
}
