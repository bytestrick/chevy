package chevy.model.entity.dynamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Archer extends Player {
    private final Vertex idle;
    private final Vertex move;
    private final Vertex attack;
    private final Vertex hit;
    private final Vertex dead;
    private final Vertex glide;
    private final Vertex sludge;
    private final Vertex fall;

    public Archer(Vector2<Integer> initPosition) {
        super(initPosition, Type.ARCHER);

        this.speed = .15f;
        this.health = 8;
        this.currentHealth = health;
        this.shield = 0;
        this.currentShield = shield;
        this.maxDamage = 5;
        this.minDamage = 3;

        this.idle = new Vertex(State.IDLE);
        this.move = new Vertex(State.MOVE, speed, true);
        this.attack = new Vertex(State.ATTACK, .6f, true);
        this.hit = new Vertex(State.HIT, .2f);
        this.dead = new Vertex(State.DEAD, .3f);
        this.glide = new Vertex(State.GLIDE, speed, true);
        this.sludge = new Vertex(State.SLUDGE, speed);
        this.fall = new Vertex(State.FALL);
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
        attack.linkState((hit));
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
    public synchronized Vertex getState(CommonState state) {
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
