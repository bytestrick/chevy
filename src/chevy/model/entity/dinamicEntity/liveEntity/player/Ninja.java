package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Ninja extends Player {
    private final State idle = new State(States.IDLE );
    private final State move = new State(States.MOVE, speed);
    private final State attack = new State(States.ATTACK, 0.04f);
    private final State hit = new State(States.HIT, .2f);
    private final State dead = new State(States.DEAD);
    private final State glide = new State(States.GLIDE, speed, true);
    private final State sludge = new State(States.SLUDGE, speed);
    private final State fall = new State(States.FALL);

    public Ninja(Vector2<Integer> initPosition) {
        super(initPosition, Type.NINJA);

        this.speed = 0.02f;
        this.health = 60;
        this.shield = 0;
        this.maxDamage = 8;
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

        stateMachine.setStateMachineName("Ninja");
        stateMachine.setInitialState(idle);
    }

    @Override
    public synchronized State getState(CommonStates state) {
        return switch ((Player.States) state) {
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