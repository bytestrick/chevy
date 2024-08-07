package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Knight extends Player {
    private final GlobalState idle = new GlobalState(State.IDLE );
    private final GlobalState move = new GlobalState(State.MOVE, speed);
    private final GlobalState attack = new GlobalState(State.ATTACK, .4f);
    private final GlobalState hit = new GlobalState(State.HIT, .2f);
    private final GlobalState dead = new GlobalState(State.DEAD, 2f);
    private final GlobalState glide = new GlobalState(State.GLIDE, speed, true);
    private final GlobalState sludge = new GlobalState(State.SLUDGE, speed);
    private final GlobalState fall = new GlobalState(State.FALL);

    public Knight(Vector2<Integer> initPosition) {
        super(initPosition, Type.KNIGHT);

        this.speed = 0.2f;
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
        hit.linkState(fall);
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
        fall.linkState(dead);

        stateMachine.setStateMachineName("Knight");
        stateMachine.setInitialState(idle);
    }

    @Override
    public synchronized GlobalState getState(CommonState state) {
        return switch ((State) state) {
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
