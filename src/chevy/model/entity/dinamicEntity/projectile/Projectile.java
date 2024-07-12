package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.utils.Vector2;

public abstract class Projectile extends DynamicEntity {

    public enum Type implements EntityCommonEnumTypes {
        ARROW,
        SLIME_SHOT;
    }
    private final Type type;

    private final DirectionsModel direction;
    private boolean isCollide = false;

    public Projectile(Vector2<Integer> initPosition, Type type, DirectionsModel direction) {
        super(initPosition, DynamicEntity.Type.PROJECTILE);
        this.type = type;
        this.direction = direction;

        this.crossable = true;
        this.safeToCross = false;

        this.drawLayer = 1;
    }


    public boolean isCollide() {
        return isCollide;
    }

    public void setCollide(boolean collide) {
        this.isCollide = collide;
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
