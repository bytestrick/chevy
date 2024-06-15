package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.EntityCommonTypes;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.utilz.Vector2;

public class Projectile extends DynamicEntity {
    private final ProjectileTypes type;
    private final DirectionsModel direction;
    private boolean collide;


    public Projectile(Vector2<Integer> initPosition, ProjectileTypes type, DirectionsModel direction) {
        super(initPosition, DynamicEntityTypes.PROJECTILE);
        this.type = type;
        this.direction = direction;

        this.updateEverySecond = 1;

        this.crossable = true;
        this.safeToCross = false;

        this.maxDamage = 2;
        this.minDamage = 1;
        this.collide = false;

        this.layer = 1;
    }


    public boolean isCollide() {
        return collide;
    }

    public void setCollide(boolean collide) {
        this.collide = collide;
    }

    public DirectionsModel getDirection() {
        return direction;
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
        return "PROJECTILE";
    }
}
