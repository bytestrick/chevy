package chevy.model.entity.dinamicEntity.liveEntity;

import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.model.entity.dinamicEntity.stateMachine.EnumState;
import chevy.utilz.Vector2;

import java.util.Random;

public abstract class LiveEntity extends DynamicEntity {
    private final LiveEntityTypes type;
    protected int health;
    protected int shield;
    protected int maxDamage;
    protected int minDamage;
    protected boolean flying;
    private boolean alive;


    public LiveEntity(Vector2<Integer> initPosition, LiveEntityTypes type) {
        super(initPosition, DynamicEntityTypes.LIVE_ENTITY);
        this.type = type;
        this.alive = true;
        this.flying = false;
    }


    public void changeHealth(int value) {
        System.out.print("vita " + this + ": " + health);

        this.health += value;
        if (health <= 0) {
            alive = false;
            this.health = 0;
        }

        System.out.println(" -> " + health);
    }

    public void changeShield(int value) {
        this.shield += value;
    }

    public int getDamage() {
        Random random = new Random();
        return random.nextInt(minDamage, maxDamage);
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isFlying() {
        return flying;
    }

    public EnumState getCurrentEumState() {
        return stateMachine.getCurrentState().getStateEnum();
    }


    @Override
    public EntityTypes getSpecificType() { return type; }

    @Override
    public EntityTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() { return type.toString(); }
}
