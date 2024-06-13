package chevy.model.entity;

import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.utilz.Vector2;

import java.util.Random;
import java.util.UUID;


public abstract class Entity {
    private final UUID ID = UUID.randomUUID();
    private final StaticEntityTypes type;
    protected int maxDamage;
    protected int minDamage;
    protected final Vector2<Integer> position;
    protected boolean safeToCross;
    protected boolean crossable;

    protected float updateEverySecond;
    private int tick;

    protected int layer;
    private boolean toDraw;


    public Entity(Vector2<Integer> initPosition, StaticEntityTypes type) {
        this.position = initPosition;
        this.type = type;

        this.updateEverySecond = 0;
        this.tick = 0;

        this.crossable = false;
        this.safeToCross = true;

        this.layer = 0;
        this.toDraw = false;
    }


    public boolean isToDraw() {
        return toDraw;
    }

    public void setToDraw(boolean toDraw) {
        this.toDraw = toDraw;
    }

    public float getUpdateEverySecond() {
        return updateEverySecond;
    }

    public int getCurrentNUpdate() {
        return tick;
    }

    public void incrementNUpdate() {
        ++tick;
    }

    public void resetNUpdate() {
        tick = 0;
    }

    public synchronized int getDamage() {
        Random random = new Random();
        return random.nextInt(minDamage, maxDamage + 1);
    }

    public EntityTypes getSpecificType() { return type; }

    public EntityTypes getGenericType() { return null; }

    public final int getRow() { return position.first; }

    public final int getCol() { return position.second; }

    public boolean isCrossable() { return crossable; }

    public boolean isSafeToCross() {
        if (crossable)
            return safeToCross;

        return false;
    }

    public int getLayer() {
        return Math.abs(this.layer);
    }

    @Override
    public String toString() {
        return "ENTITY";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return ID.equals(entity.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
