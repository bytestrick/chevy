package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.EntityType;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Utils;

import java.awt.*;

public abstract class Enemy extends LiveEntity {
    /**
     * Collectable objects that enemies can drop
     */
    private static final Collectable.Type[] DROPPABLE_COLLECTABLE = {
            Collectable.Type.COIN,
            Collectable.Type.KEY,
            Collectable.Type.HEALTH
    };
    /**
     * Probability of dropping collectable objects
     */
    private static final int[] DROPPABLE_COLLECTABLE_PROB = {40, 10, 15};
    private static final int DROP_PROBABILITY = 100;
    private final Vertex move, attack, hit, dead;
    private final Type type;
    Vertex idle, invincibility;
    private boolean canAttack;

    Enemy(Point position, Type type, float idleDuration,
          float hitDuration) {
        super(position, LiveEntity.Type.ENEMY);
        this.type = type;

        idle = new Vertex(State.IDLE, idleDuration);
        move = new Vertex(State.MOVE, .5f);
        attack = new Vertex(State.ATTACK, .5f);
        hit = new Vertex(State.HIT, hitDuration);
        dead = new Vertex(State.DEAD, .3f);

        drawLayer = 3;
        initStateMachine();
    }

    public static void setDropPercentage(int i, int newDropPercentage) {
        DROPPABLE_COLLECTABLE_PROB[i] = Math.clamp(newDropPercentage, 0, 100);
    }

    public static int getDropPercentage(int i) {
        return DROPPABLE_COLLECTABLE_PROB[i];
    }

    public static boolean canDrop() {
        return Utils.isOccurring(DROP_PROBABILITY);
    }

    public static Collectable.Type getDrop() {
        int index = Utils.isOccurring(DROPPABLE_COLLECTABLE_PROB);
        if (index == -1) {
            return null;
        }
        return DROPPABLE_COLLECTABLE[index];
    }

    /**
     * @return {@code true} if the player can attack, {@code false} otherwise
     */
    public boolean canAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    @Override
    public EntityType getType() {
        return type;
    }

    @Override
    public EntityType getGenericType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return type.toString();
    }

    private void initStateMachine() {
        stateMachine.setInitialState(idle);

        idle.linkVertex(move);
        idle.linkVertex(attack);
        idle.linkVertex(hit);
        move.linkVertex(idle);
        move.linkVertex(hit);
        attack.linkVertex(idle);
        attack.linkVertex(hit);
        hit.linkVertex(idle);
        hit.linkVertex(dead);
    }

    @Override
    public synchronized Vertex getState(EntityState state) {
        return switch ((State) state) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
            case INVINCIBILITY -> invincibility;
        };
    }

    public enum State implements EntityState {MOVE, ATTACK, HIT, DEAD, IDLE, INVINCIBILITY}

    public enum Type implements EntityType {
        WRAITH, ZOMBIE, SKELETON, SLIME, BIG_SLIME, BEETLE
    }
}
