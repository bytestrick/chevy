package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.Entity;
import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.model.entity.EntityTypes;
import chevy.utilz.Vector2;

public abstract class Environment extends Entity {
    private final EnvironmentTypes type;


    public Environment(Vector2<Integer> initVelocity, EnvironmentTypes type) {
        super(initVelocity, StaticEntityTypes.ENVIRONMENT);
        this.type = type;

        this.layer = 1;
    }


    @Override
    public EntityTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
