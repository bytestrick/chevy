package chevy.model.entity.dynamicEntity.liveEntity.player;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Ninja extends Player {
    private final Vertex idle;
    private final Vertex move;
    private final Vertex attack;
    private final Vertex hit;
    private final Vertex dead;
    private final Vertex glide;
    private final Vertex sludge;
    private final Vertex fall;

    public Ninja(Vector2<Integer> initPosition) {
        super(initPosition, Type.NINJA);

        this.speed = 0.1f;
        this.health = 6;
        this.currentHealth = health;
        this.shield = 2;
        this.currentShield = shield;
        this.maxDamage = 5;
        this.minDamage = 4;

        this.idle = new Vertex(State.IDLE);
        this.move = new Vertex(State.MOVE, speed, true);
        this.attack = new Vertex(State.ATTACK, .3f, true);
        this.hit = new Vertex(State.HIT, .2f);
        this.dead = new Vertex(State.DEAD, .3f);
        this.glide = new Vertex(State.GLIDE, speed, true);
        this.sludge = new Vertex(State.SLUDGE, speed);
        this.fall = new Vertex(State.FALL, 0.2f);
        initStateMachine();
    }

    private void initStateMachine() {
        idle.linkVertex(move);
        idle.linkVertex(attack);
        idle.linkVertex(hit);
        idle.linkVertex(fall);
        hit.linkVertex(idle);
        hit.linkVertex(dead);
        hit.linkVertex(move);
        hit.linkVertex(fall);
        move.linkVertex(glide);
        move.linkVertex(hit);
        move.linkVertex(fall);
        move.linkVertex(sludge);
        move.linkVertex(idle);
        move.linkVertex(attack);
        attack.linkVertex(idle);
        attack.linkVertex(move);
        attack.linkVertex(hit);
        glide.linkVertex(idle);
        glide.linkVertex(fall);
        glide.linkVertex(hit);
        glide.linkVertex(sludge);
        sludge.linkVertex(idle);
        fall.linkVertex(idle);
        fall.linkVertex(hit);
        fall.linkVertex(dead);

        stateMachine.setStateMachineName("Ninja");
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
