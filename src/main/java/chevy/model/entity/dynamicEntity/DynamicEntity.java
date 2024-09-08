package chevy.model.entity.dynamicEntity;

import chevy.model.entity.EntityType;
import chevy.model.entity.Entity;
import chevy.utils.Vector2;

public abstract class DynamicEntity extends Entity {
    private final Type type;

    public DynamicEntity(Vector2<Integer> initPosition, Type type) {
        super(initPosition, Entity.Type.DYNAMIC);
        this.type = type;
        shouldUpdate = true;
    }

    public void changePosition(Vector2<Integer> velocity) {position.change(velocity);}

    protected abstract void initStateMachine();

    @Override
    public EntityType getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return "DYNAMIC ENTITY";}

    public enum Type implements EntityType {LIVE_ENTITY, PROJECTILE}
}