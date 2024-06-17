package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Bat extends Enemy {
    private final State idle = new State(BatStates.IDLE);
    private final State move = new State(BatStates.MOVE);
    private final State attack = new State(BatStates.ATTACK);
    private final State hit = new State(BatStates.HIT);
    private final State dead = new State(BatStates.DEAD);


    public Bat(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.BAT);
        this.flying = true;
        this.health = 3;
        this.maxDamage = 2;
        this.minDamage = 1;

        this.updateEverySecond = 0.5f;

        stateMachine.setStateMachineName("Bat");
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
}
