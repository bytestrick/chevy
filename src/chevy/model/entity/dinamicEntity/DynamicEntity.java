package chevy.model.entity.dinamicEntity;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.model.entity.stateMachine.StateMachine;
import chevy.utils.Vector2;

public abstract class DynamicEntity extends Entity {
    public enum Type implements EntityCommonEnumTypes {
        LIVE_ENTITY,
        PROJECTILE;


    }
    private final Type type;
    private boolean canRemove = false;
    protected DirectionsModel direction = DirectionsModel.getRandom();


    public DynamicEntity(Vector2<Integer> initPosition, Type type) {
        super(initPosition, Entity.Type.DYNAMIC);
        this.type = type;
    }


    public void changePosition(Vector2<Integer> velocity) {
        this.position.change(velocity);
    }

    public DirectionsModel getDirection() {
        return direction;
    }

    public void setDirection(DirectionsModel direction) {
        this.direction = direction;
    }

    public void removeToUpdate() {
        canRemove = true;
    }

    public boolean canRemoveToUpdate() {
        return canRemove;
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
        return "DYNAMIC ENTITY";
    }
}