package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Knight extends Player {
    private final State idle = new State(EnumState.IDLE);
    private final State move = new State(EnumState.MOVE, speed);
    private final State attack = new State(EnumState.ATTACK);
    private final State hit = new State(EnumState.HIT, 0.1f);
    private final State dead = new State(EnumState.DEAD);
    private final State glide = new State(EnumState.GLIDE, speed, true);
    private final State sludge = new State(EnumState.SLUDGE, speed);
    private final State fall = new State(EnumState.FALL);


    public Knight(Vector2<Integer> initPosition) {
        super(initPosition, Type.KNIGHT);
        this.health = 100;
        this.shield = 2;
        this.maxDamage = 7;
        this.minDamage = 5;

        initStateMachine();
    }


    private void initStateMachine() {
        idle.linkState(move);
        idle.linkState(attack);
        idle.linkState(hit);
        idle.linkState(fall);
        hit.linkState(idle);
        hit.linkState(dead);
        hit.linkState(move);
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
        glide.linkState(sludge);
        sludge.linkState(idle);
        fall.linkState(idle);
        fall.linkState(hit);
        fall.linkState(dead);

        stateMachine.setStateMachineName("Knight");
        stateMachine.setInitialState(idle);
    }

    @Override
    public synchronized State getState(CommonEnumStates commonEnumStates) {
        Player.EnumState playerState = (Player.EnumState) commonEnumStates;
        return switch (playerState) {
            case IDLE -> idle;
            case ATTACK -> attack;
            case MOVE -> move;
            case DEAD -> dead;
            case HIT -> hit;
            case SLUDGE -> sludge;
            case FALL -> fall;
            case GLIDE -> glide;
        };
    }
}
