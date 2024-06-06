package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.EntityTypes;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.EnvironmentTypes;
import chevy.utilz.Vector2;

public abstract class Traps extends Environment {
    private final TrapsTypes type;


    public Traps(Vector2<Integer> initVelocity, TrapsTypes type) {
        super(initVelocity, EnvironmentTypes.TRAP);
        this.type = type;
        this.crossable = true;
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
