package chevy.model.entity.dynamicEntity.liveEntity;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.utils.Log;
import chevy.utils.Vector2;

public abstract class LiveEntity extends DynamicEntity {
    private final Type type;
    protected int health;
    protected int currentHealth;
    protected int shield;
    protected int currentShield;
    protected boolean flying;
    private boolean alive;

    public LiveEntity(Vector2<Integer> initPosition, Type type) {
        super(initPosition, DynamicEntity.Type.LIVE_ENTITY);
        this.type = type;
        this.alive = true;
        this.flying = false;
    }

    public synchronized void decreaseHealthShield(int value) {
        String logMessage = "Vita " + this + ": " + currentHealth;
        String logMessage2 = "Scudo " + this + ": " + currentShield;

        value = -Math.abs(value);

        if (currentShield > 0) {
            currentShield += value;
            if (currentShield < 0) {
                value = currentShield;
                currentShield = 0;
            } else {
                value = 0;
            }
        }
        if (currentShield <= 0) {
            currentHealth += value;
            if (currentHealth <= 0) {
                currentHealth = 0;
            }
        }

        Log.info(logMessage + " -> " + currentHealth + "\n    " + logMessage2 + " -> " + currentShield);
    }

    public synchronized void increaseCurrentHealth(int value) {
        int newHealth = currentHealth + Math.abs(value);
        currentHealth = Math.min(newHealth, health);
    }

    public synchronized void increaseCurrentShield(int value) {
        int newShield = currentShield + Math.abs(value);
        currentShield = Math.min(newShield, shield);
    }

    public synchronized void kill() {
        if (currentHealth <= 0) {
            alive = false;
        }
    }

    public synchronized int getCurrentHealth() {return currentHealth;}

    public int getCurrentShield() {return currentShield;}

    public synchronized void changeShield(int value) {this.shield = value;}

    public synchronized void changeHealth(int value) {this.health = value;}

    public synchronized int getHealth() {return health;}

    public synchronized int getShield() {return shield;}

    public synchronized boolean isDead() {return !alive;}

    @Override
    public CommonEntityType getSpecificType() {return type;}

    @Override
    public CommonEntityType getGenericType() {return super.getSpecificType();}

    @Override
    public String toString() {return type.toString();}

    public enum Type implements CommonEntityType {PLAYER, ENEMY}
}