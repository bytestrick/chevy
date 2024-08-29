package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.Entity;
import chevy.model.entity.CommonEntityType;
import chevy.utils.Vector2;

public abstract class Environment extends Entity {
    public enum Type implements CommonEntityType {
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

        this.drawLayer = 1;
    }


    @Override
    public CommonEntityType getSpecificType() {
        return type;
    }

    @Override
    public CommonEntityType getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
