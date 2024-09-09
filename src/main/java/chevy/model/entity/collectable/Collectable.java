package chevy.model.entity.collectable;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityType;
import chevy.model.entity.stateMachine.EntityState;

import java.awt.Point;

public abstract class Collectable extends Entity {
    private final Type type;
    private boolean collected;

    public Collectable(Point initPosition, Type type) {
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
    public EntityType getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return type.toString();}

    public enum Type implements EntityType {POWER_UP, COIN, HEALTH, KEY}

    public enum State implements EntityState {IDLE, COLLECTED}
}