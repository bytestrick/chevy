package chevy.model.entity;

import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.utilz.Vector2;

import java.util.Objects;
import java.util.Random;


public abstract class Entity {
    private final StaticEntityTypes type;
    protected int maxDamage;
    protected int minDamage;
    protected final Vector2<Integer> position;
    protected boolean crossable = false;
    protected int updateEverySecond;
    private int nUpdate;


    public Entity(Vector2<Integer> initPosition, StaticEntityTypes type) {
        this.position = initPosition;
        this.type = type;
        this.updateEverySecond = 3;
        this.nUpdate = 0;
    }


    public int getUpdateEverySecond() {
        return updateEverySecond;
    }

    public int getCurrentNUpdate() {
        return nUpdate;
    }

    public void incrementNUpdate() {
        ++nUpdate;
    }

    public void resetNUpdate() {
        nUpdate = 0;
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

    @Override
    public String toString() {
        return "ENTITY";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (crossable != entity.crossable) return false;
        if (type != entity.type) return false;
        return Objects.equals(position, entity.position);
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (crossable ? 1 : 0);
        return result;
    }
}
