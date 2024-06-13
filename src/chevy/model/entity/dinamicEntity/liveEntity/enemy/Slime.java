package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Slime extends Enemy {
    private final State idle = new State(SlimeStates.IDLE, 2);
    private final State move = new State(SlimeStates.MOVE, 2);
    private final State attack = new State(SlimeStates.ATTACK, 2);
    private final State hit = new State(SlimeStates.HIT);
    private final State dead = new State(SlimeStates.DEAD);


    public Slime(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.SLIME);
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
        hit.linkState(idle);
        hit.linkState(dead);
    }
}
