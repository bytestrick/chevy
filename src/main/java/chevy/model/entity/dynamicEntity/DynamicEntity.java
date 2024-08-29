package chevy.model.entity.dynamicEntity;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.Entity;
import chevy.utils.Vector2;

public abstract class DynamicEntity extends Entity {
    private final Type type;
    private final boolean canRemove = false;
    protected DirectionsModel direction = DirectionsModel.getRandom();

    public DynamicEntity(Vector2<Integer> initPosition, Type type) {
        super(initPosition, Entity.Type.DYNAMIC);
        this.type = type;

        this.mustBeUpdate = true;
    }

    public void changePosition(Vector2<Integer> velocity) {
        this.position.change(velocity);
    }

    public DirectionsModel getDirection() { return direction; }

    public void setDirection(DirectionsModel direction) { this.direction = direction; }

    @Override
    public CommonEntityType getSpecificType() { return type; }

    @Override
    public CommonEntityType getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() { return "DYNAMIC ENTITY"; }

    public enum Type implements CommonEntityType {
        LIVE_ENTITY, PROJECTILE
    }
}