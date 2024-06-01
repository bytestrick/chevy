package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.BigSlimeStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.model.entity.dinamicEntity.stateMachine.ZombieStates;
import chevy.utilz.Vector2;

public class BigSlime extends Enemy {
    private final State idle = new State(BigSlimeStates.IDLE);
    private final State move = new State(BigSlimeStates.MOVE);
    private final State attack = new State(BigSlimeStates.ATTACK);
    private final State hit = new State(BigSlimeStates.HIT);
    private final State dead = new State(BigSlimeStates.DEAD);


    public BigSlime(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.BIG_SLIME);
        this.health = 10;
        this.maxDamage = 3;
        this.minDamage = 2;

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
