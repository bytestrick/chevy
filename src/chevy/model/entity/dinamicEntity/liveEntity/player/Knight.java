package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

import java.util.Random;

public class Knight extends Player {
    private final GlobalState idle;
    private final GlobalState move;
    private final GlobalState attack;
    private final GlobalState hit;
    private final GlobalState dead;
    private final GlobalState glide;
    private final GlobalState sludge;
    private final GlobalState fall;

    public Knight(Vector2<Integer> initPosition) {
        super(initPosition, Type.KNIGHT);

        this.speed = .3f;
        this.health = 10;
        this.currentHealth = health;
        this.shield = 5;
        this.currentShield = shield;
        this.maxDamage = 7;
        this.minDamage = 5;

        this.idle = new GlobalState(State.IDLE);
        this.move = new GlobalState(State.MOVE, speed);
        this.attack = new GlobalState(State.ATTACK, .5f);
        this.hit = new GlobalState(State.HIT, .2f);
        this.dead = new GlobalState(State.DEAD, .3f);
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
        hit.linkState(fall);
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
