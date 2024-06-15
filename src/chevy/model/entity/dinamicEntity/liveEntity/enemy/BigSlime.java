package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class BigSlime extends Enemy {
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


    public BigSlime(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.BIG_SLIME);
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
}
