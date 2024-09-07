package chevy.model.entity.collectable;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.Entity;
import chevy.utils.Vector2;

public abstract class Collectable extends Entity {
    private final Type type;
    private boolean collected;

    public Collectable(Vector2<Integer> initPosition, Type type) {
        super(initPosition, Entity.Type.COLLECTABLE);
        this.type = type;

        shouldUpdate = true;
        crossable = true;
        safeToCross = false;
        drawLayer = 3;
    }

    public void collect() {collected = true;}

    public boolean isCollected() {return collected;}

    @Override
    public CommonEntityType getSpecificType() {return type;}

    @Override
    public CommonEntityType getGenericType() {return super.getSpecificType();}

    @Override
    public String toString() {return type.toString();}

    public enum Type implements CommonEntityType {POWER_UP, COIN, HEALTH, KEY}
}