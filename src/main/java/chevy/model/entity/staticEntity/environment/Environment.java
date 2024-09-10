package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityType;

import java.awt.Point;

public abstract class Environment extends Entity {
    private final Type type;

    public Environment(Point position, Type type) {
        super(position, Entity.Type.ENVIRONMENT);
        this.type = type;
        drawLayer = 1;
    }

    @Override
    public EntityType getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return type.toString();}

    public enum Type implements EntityType {GROUND, WALL, STAIR, TRAP, CHEST}
}
