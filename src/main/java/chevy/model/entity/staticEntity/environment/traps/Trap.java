package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.EntityType;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.staticEntity.environment.Environment;

import java.awt.*;

public abstract class Trap extends Environment {
    private final Type type;

    Trap(Point position, Type type) {
        super(position, Environment.Type.TRAP);
        this.type = type;
        crossable = true;
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
        return type.toString();
    }

    public Direction getShotDirection() {
        return null;
    }

    public enum Type implements EntityType {SLUDGE, VOID, SPIKED_FLOOR, TRAPDOOR, TOTEM, ICY_FLOOR}
}
