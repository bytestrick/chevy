package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Archer extends Player {
    private final GlobalState idle = new GlobalState(State.IDLE );
    private final GlobalState move = new GlobalState(State.MOVE, .1f);
    private final GlobalState attack = new GlobalState(State.ATTACK, .5f);
    private final GlobalState hit = new GlobalState(State.HIT, .2f);
    private final GlobalState dead = new GlobalState(State.DEAD);
    private final GlobalState glide = new GlobalState(State.GLIDE, speed, true);
    private final GlobalState sludge = new GlobalState(State.SLUDGE, speed);
    private final GlobalState fall = new GlobalState(State.FALL);

    public Archer(Vector2<Integer> initPosition) {
        super(initPosition, Type.ARCHER);

        this.speed = 0.1f;
        this.health = 80;
        this.shield = 0;
        this.maxDamage = 10;
        this.minDamage = 6;

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

        stateMachine.setStateMachineName("Archer");
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