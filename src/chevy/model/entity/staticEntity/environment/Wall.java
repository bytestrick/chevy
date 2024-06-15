package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.EntityCommonTypes;
import chevy.utilz.Vector2;

public class Wall extends Environment {
    private final WallTypes type;

    public Wall(Vector2<Integer> initVelocity, WallTypes type) {
        super(initVelocity, EnvironmentTypes.WALL);
        this.type = type;
    }

    @Override
    public WallTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonTypes getGenericType() {
        return super.getSpecificType();
    }
}
