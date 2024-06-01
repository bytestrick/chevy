package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.FrogStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Frog extends Enemy {
    private final State idle = new State(FrogStates.IDLE);
    private final State move = new State(FrogStates.MOVE);
    private final State attack = new State(FrogStates.ATTACK);
    private final State hit = new State(FrogStates.HIT);
    private final State dead = new State(FrogStates.DEAD);


    public Frog(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.FROG);
        this.health = 10;
        this.maxDamage = 5;
        this.minDamage = 3;

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
