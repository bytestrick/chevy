package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.EntityTypes;
import chevy.utilz.Vector2;

public class Ground extends Environment {
    private final GroundTypes type;


    public Ground(Vector2<Integer> initVelocity, GroundTypes type) {
        super(initVelocity, EnvironmentTypes.GROUND);
        this.type = type;
        this.crossable = true;

        this.layer = 0;
    }


    @Override
    public GroundTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityTypes getGenericType() {
        return super.getSpecificType();
    }
}
