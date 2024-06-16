package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityCommonEnumTypes;
import chevy.utilz.Vector2;

public abstract class Environment extends Entity {
    public enum Type implements EntityCommonEnumTypes {
        GROUND,
        WALL,
        STAIR,
        BARRIER,
        TRAP,
        CHEST
    }
    private final Type type;


    public Environment(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Entity.Type.ENVIRONMENT);
        this.type = type;

        this.layer = 1;
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
    public String toString() {
        return type.toString();
    }
}
