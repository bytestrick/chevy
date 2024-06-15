package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Frog extends Enemy {
    public enum EnumState implements CommonEnumStates {
        MOVE,
        ATTACK,
        HIT,
        DEAD,
        IDLE
    }
    private final State idle = new State(EnumState.IDLE);
    private final State move = new State(EnumState.MOVE);
    private final State attack = new State(EnumState.ATTACK);
    private final State hit = new State(EnumState.HIT);
    private final State dead = new State(EnumState.DEAD);


    public Frog(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.FROG);
        this.health = 10;
        this.maxDamage = 5;
        this.minDamage = 3;

        this.updateEverySecond = 2.f;

        stateMachine.setStateMachineName("Frog");
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
}
