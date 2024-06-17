package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.model.entity.dinamicEntity.stateMachine.ZombieStates;
import chevy.utilz.Vector2;

public class Zombie extends Enemy {
    private final State idle = new State(ZombieStates.IDLE);
    private final State move = new State(ZombieStates.MOVE);
    private final State attack = new State(ZombieStates.ATTACK);
    private final State hit = new State(ZombieStates.HIT);
    private final State dead = new State(ZombieStates.DEAD);


    public Zombie(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.ZOMBIE);
        this.health = 10;
        this.maxDamage = 3;
        this.minDamage = 2;

        this.updateEverySecond = 1.f;

        stateMachine.setStateMachineName("Zombie");
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
        hit.linkState(idle);
        hit.linkState(dead);
    }
}
