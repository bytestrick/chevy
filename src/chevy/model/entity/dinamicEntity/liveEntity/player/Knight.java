package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Vector2;

public class Knight extends Player {
    private final State idle = new State(States.IDLE);
    private final State move = new State(States.MOVE);
    private final State attack = new State(States.ATTACK);
    private final State hit = new State(States.HIT);
    private final State dead = new State(States.DEAD);
    private final State glide = new State(States.GLIDE);
    private final State sludge = new State(States.SLUDGE);
    private final State fall = new State(States.FALL);


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
        idle.linkState(fall);
        hit.linkState(idle);
        hit.linkState(dead);
        move.linkState(glide);
        move.linkState(hit);
        move.linkState(fall);
        move.linkState(sludge);
        move.linkState(idle);
        move.linkState(attack);
        attack.linkState(idle);
        attack.linkState(move);
        glide.linkState(idle);
        glide.linkState(fall);
        glide.linkState(hit);
        sludge.linkState(idle);
        fall.linkState(idle);
        fall.linkState(hit);
        fall.linkState(dead);
    }
}
