package chevy.model.entity.staticEntity.environment;

import chevy.utilz.Vector2;

public class Ground extends Environment {
    private final GroundTypes type;


    public Ground(Vector2<Integer> initVelocity, GroundTypes type) {
        super(initVelocity, EnvironmentTypes.GROUND);
        this.type = type;
    }


    @Override
    public GroundTypes getSpecificType() {
        return type;
    }
}
