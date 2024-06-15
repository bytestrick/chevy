package chevy.model.entity.dinamicEntity.liveEntity;

import chevy.model.entity.EntityCommonTypes;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.utilz.Vector2;

public abstract class LiveEntity extends DynamicEntity {
    private final LiveEntityTypes type;
    protected int health;
    protected int shield;
    protected boolean flying;
    private boolean alive;


    public LiveEntity(Vector2<Integer> initPosition, LiveEntityTypes type) {
        super(initPosition, DynamicEntityTypes.LIVE_ENTITY);
        this.type = type;
        this.alive = true;
        this.flying = false;
    }


    public synchronized void changeHealth(int value) {
        System.out.print("vita " + this + ": " + health);

        this.health += value;
        if (health <= 0) {
            alive = false;
            this.health = 0;
        }

        System.out.println(" -> " + health);
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
    public EntityCommonTypes getSpecificType() { return type; }

    @Override
    public EntityCommonTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() { return type.toString(); }
}
