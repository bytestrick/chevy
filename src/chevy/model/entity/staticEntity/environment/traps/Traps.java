package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.EntityCommonTypes;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.EnvironmentTypes;
import chevy.utilz.Vector2;

public abstract class Traps extends Environment {
    private final TrapsTypes type;
    protected boolean canHitFlingEntity;



    public Traps(Vector2<Integer> initVelocity, TrapsTypes type) {
        super(initVelocity, EnvironmentTypes.TRAP);
        this.type = type;
        this.crossable = true;
        this.canHitFlingEntity = false;
    }


    @Override
    public EntityCommonTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
