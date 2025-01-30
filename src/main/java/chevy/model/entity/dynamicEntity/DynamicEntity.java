package chevy.model.entity.dynamicEntity;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityType;

import java.awt.*;

public abstract class DynamicEntity extends Entity {
    private final Type type;

    public DynamicEntity(Point position, Type type) {
        super(position, Entity.Type.DYNAMIC);
        this.type = type;
        shouldUpdate = true;
    }

    public void setPosition(Point position) {
        this.position.setLocation(position);
    }

    @Override
    public EntityType getType() {
        return type;
    }

    @Override
    public EntityType getGenericType() {
        return super.getType();
    }

    @Override
    public String toString() {
        return "DYNAMIC ENTITY";
    }

    public enum Type implements EntityType {LIVE_ENTITY, PROJECTILE}
}
