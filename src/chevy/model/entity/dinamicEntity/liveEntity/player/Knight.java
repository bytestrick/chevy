package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Knight extends Player {
    private final State idle = new State(PlayerStates.IDLE);
    private final State move = new State(PlayerStates.MOVE, true);
    private final State attack = new State(PlayerStates.ATTACK);
    private final State hit = new State(PlayerStates.HIT);
    private final State dead = new State(PlayerStates.DEAD);


    public Knight(Vector2<Integer> initPosition) {
        super(initPosition, PlayerTypes.KNIGHT);
        this.health = 10;
        this.shield = 2;
        this.maxDamage = 7;
        this.minDamage = 5;

        stateMachine.setStateMachineName("Knight");
        stateMachine.setInitialState(idle);
        initStateMachine();
    }


    private void initStateMachine() {
        idle.linkState(move);
        idle.linkState(attack);
        idle.linkState(hit);
        hit.linkState(idle);
        hit.linkState(dead);
        move.linkState(idle);
        move.linkState(attack);
        attack.linkState(idle);
        attack.linkState(move);
    }
}
