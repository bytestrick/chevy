package chevy.model.entity.dinamicEntity.player;

import chevy.model.entity.dinamicEntity.stateMachine.KnightStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Knight extends Player {
    private final State idle = new State(KnightStates.IDLE);
    private final State move = new State(KnightStates.MOVE);
    private final State attack = new State(KnightStates.ATTACK);
    private final State hit = new State(KnightStates.HIT);


    public Knight(Vector2<Integer> initVelocity) {
        super(initVelocity, PlayerTypes.KNIGHT);
        this.health = 10;
        this.shield = 2;
        this.maxDamage = 7;
        this.minDamage = 5;

        stateMachine.setInitialState(idle);
        initStateMachine();
    }


    private void initStateMachine() {
        idle.linkState(move);
        idle.linkState(attack);
        move.linkState(idle);
        attack.linkState(idle);
    }
}
