package chevy.model.entity.dinamicEntity.liveEntity;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.utils.Vector2;

public abstract class LiveEntity extends DynamicEntity {
    protected int health;
    protected int shield;
    private boolean alive;
    protected boolean flying;
    public enum Type implements EntityCommonEnumTypes {
        PLAYER,
        ENEMY
    }
    private final Type type;


    public LiveEntity(Vector2<Integer> initPosition, Type type) {
        super(initPosition, DynamicEntity.Type.LIVE_ENTITY);
        this.type = type;
        this.alive = true;
        this.flying = false;
    }


    public synchronized void changeHealth(int value) {
        System.out.print("vita " + this + ": " + health);

        this.health += value;
        if (health <= 0) {
            this.health = 0;
        }

        System.out.println(" -> " + health);
    }

    public synchronized int getHealth() {
        return health;
    }

    public synchronized void kill() {
        if (health <= 0)
            alive = false;
    }

    public synchronized void changeShield(int value) {
        this.shield += value;
    }

    public synchronized boolean isAlive() {
        return alive;
    }

    public synchronized boolean isFlying() {
        return flying;
    }

    @Override
    public EntityCommonEnumTypes getSpecificType() { return type; }

    @Override
    public EntityCommonEnumTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() { return type.toString(); }
}
