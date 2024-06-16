package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.utilz.Vector2;

public class Projectile extends DynamicEntity {
    private final DirectionsModel direction;
    private boolean collide;
    public enum Type implements EntityCommonEnumTypes {
        ARROW,
        FIRE_BALL;
    }
    private final Type type;


    public Projectile(Vector2<Integer> initPosition, Type type, DirectionsModel direction) {
        super(initPosition, DynamicEntity.Type.PROJECTILE);
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
    public EntityCommonEnumTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonEnumTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return "PROJECTILE";
    }
}
