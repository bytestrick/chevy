package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.EntityType;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Utils;
import chevy.utils.Vector2;

public abstract class Enemy extends LiveEntity {
    /**
     * Oggetti collezionabili che i nemici possono rilasciare
     */
    private static final Collectable.Type[] DROPPABLE_COLLECTABLE = {
            Collectable.Type.COIN,
            Collectable.Type.KEY,
            Collectable.Type.HEALTH};
    /**
     * Probabilità di rilascio degli oggetti collezionabili
     */
    private static final int[] DROPPABLE_COLLECTABLE_PROB = {40, 10, 15};
    private static final int DROP_PROBABILITY = 100;
    protected final Vertex move;
    protected final Vertex attack;
    protected final Vertex hit;
    protected final Vertex dead;
    private final Type type;
    protected Vertex idle;
    Vertex invincibility;
    private boolean canAttack;

    public Enemy(Vector2<Integer> initPosition, Type type, float idleDuration, float moveDuration
            , float attackDuration, float hitDuration, float deadDuration) {
        super(initPosition, LiveEntity.Type.ENEMY);
        this.type = type;

        idle = new Vertex(Skeleton.State.IDLE, idleDuration);
        move = new Vertex(Skeleton.State.MOVE, moveDuration);
        attack = new Vertex(Skeleton.State.ATTACK, attackDuration);
        hit = new Vertex(Skeleton.State.HIT, hitDuration);
        dead = new Vertex(Skeleton.State.DEAD, deadDuration);

        drawLayer = 2;
    }

    public static void changeDropPercentage(int i, int newDropPercentage) {
        DROPPABLE_COLLECTABLE_PROB[i] = Math.clamp(newDropPercentage, 0, 100);
    }

    public static int getDropPercentage(int i) {return DROPPABLE_COLLECTABLE_PROB[i];}

    public static boolean canDrop() {return Utils.isOccurring(DROP_PROBABILITY);}

    public static Collectable.Type getDrop() {
        int index = Utils.isOccurring(DROPPABLE_COLLECTABLE_PROB);
        if (index == -1) {
            return null;
        }
        return DROPPABLE_COLLECTABLE[index];
    }

    /**
     * @return {@code true} se il player è in grado di attaccare, {@code false} altrimenti
     */
    public boolean canAttack() {return canAttack;}

    public void setCanAttack(boolean canAttack) {this.canAttack = canAttack;}

    @Override
    public EntityType getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return type.toString();}

    @Override
    protected void initStateMachine() {
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