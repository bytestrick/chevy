package chevy.model.entity.staticEntity.powerUp;

import chevy.model.entity.Entity;
import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.model.entity.EntityTypes;
import chevy.utilz.Vector2;

public abstract class PowerUp extends Entity {
    private final PowerUpTypes type;


    public PowerUp(Vector2<Integer> initVelocity, PowerUpTypes type) {
        super(initVelocity, StaticEntityTypes.POWER_UP);
        this.type = type;
    }

    @Override
    public EntityTypes getSpecificType() {
        return type;
    }

    @Override
    public String toString() { return type.toString(); }
}
