package chevy.model.entity.collectable;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityCommonEnumTypes;
import chevy.utils.Vector2;

public abstract class Collectable extends Entity {
    public enum Type implements EntityCommonEnumTypes {
        POWER_UP,
        COIN,
        HEALTH,
        KEY
    }
    private final Type type;
    private boolean collected = false;

    public Collectable(Vector2<Integer> initPosition, Type type) {
        super(initPosition, Entity.Type.COLLECTABLE);
        this.type = type;

        this.mustBeUpdate = true;
        this.crossable = true;
        this.safeToCross = true;
        this.drawLayer = 1;
    }


    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }


    @Override
    public EntityCommonEnumTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonEnumTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() { return type.toString(); }
}
