package chevy.model.entity.dinamicEntity.liveEntity;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.utils.Log;
import chevy.utils.Vector2;

public abstract class LiveEntity extends DynamicEntity {
    private final Type type;
    protected int health;
    protected int shield;
    protected boolean flying;
    private boolean alive;

    public LiveEntity(Vector2<Integer> initPosition, Type type) {
        super(initPosition, DynamicEntity.Type.LIVE_ENTITY);
        this.type = type;
        this.alive = true;
        this.flying = false;
    }

    public synchronized void changeHealth(int value) {
        String logMessage = "Vita " + this + ": " + health;

        this.health += value;
        if (health <= 0) {
            this.health = 0;
        }

        Log.info(logMessage + " -> " + health);
    }

    public synchronized void kill() {
        if (health <= 0) {
            alive = false;
        }
    }

    public synchronized int getHealth() { return health; }

    public synchronized void changeShield(int value) { this.shield += value; }

    public synchronized boolean isDead() { return !alive; }

    public synchronized boolean isFlying() { return flying; }

    @Override
    public CommonEntityType getSpecificType() { return type; }

    @Override
    public CommonEntityType getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() { return type.toString(); }

    public enum Type implements CommonEntityType {
        PLAYER, ENEMY
    }
}