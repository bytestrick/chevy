package chevy.model.entity.dinamicEntity;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityTypes;
import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.utilz.Vector2;

public abstract class DynamicEntity extends Entity {
    private final DynamicEntityTypes type;


    public DynamicEntity(Vector2<Integer> initVelocity, DynamicEntityTypes type) {
        super(initVelocity, StaticEntityTypes.DYNAMIC);
        this.type = type;
    }


    protected void changeVelocity(Vector2<Integer> velocity) {
        velocity.changePosition(velocity);
    }

    public void move(Directions direction) {
    }



    @Override
    public EntityTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityTypes getGenericType() {
        return super.getGenericType();
    }

    @Override
    public String toString() {
        return "Dynamic Entity";
    }
}