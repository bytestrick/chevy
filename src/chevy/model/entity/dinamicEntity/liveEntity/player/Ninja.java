package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Ninja extends Player {
    private final GlobalState idle;
    private final GlobalState move;
    private final GlobalState attack;
    private final GlobalState hit;
    private final GlobalState dead;
    private final GlobalState glide;
    private final GlobalState sludge;
    private final GlobalState fall;

    public Ninja(Vector2<Integer> initPosition) {
        super(initPosition, Type.NINJA);

        this.speed = 0.1f;
        this.health = 60;
        this.shield = 0;
        this.maxDamage = 8;
        this.minDamage = 6;

        this.idle = new GlobalState(State.IDLE);
        this.move = new GlobalState(State.MOVE, speed);
        this.attack = new GlobalState(State.ATTACK, .5f);
        this.hit = new GlobalState(State.HIT, .2f);
        this.dead = new GlobalState(State.DEAD);
        this.glide = new GlobalState(State.GLIDE, speed, true);
        this.sludge = new GlobalState(State.SLUDGE, speed);
        this.fall = new GlobalState(State.FALL);
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
        attack.linkState(hit);
        glide.linkState(idle);
        glide.linkState(fall);
        glide.linkState(hit);
        glide.linkState(sludge);
        sludge.linkState(idle);
        fall.linkState(idle);
        fall.linkState(hit);
        fall.linkState(dead);

        stateMachine.setStateMachineName("Ninja");
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
