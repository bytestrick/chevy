package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.utilz.Vector2;

public class Projectile extends DynamicEntity {
    private final ProjectileTypes type;
    private final DirectionsModel direction;


    public Projectile(Vector2<Integer> initPosition, ProjectileTypes type, DirectionsModel direction) {
        super(initPosition, DynamicEntityTypes.PROJECTILE);
        this.type = type;
        this.direction = direction;
        this.updateEverySecond = 1;
        this.crossable = true;
        this.maxDamage = 2;
        this.minDamage = 1;
    }


    public DirectionsModel getDirection() {
        return direction;
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
        return "PROJECTILE";
    }
}
