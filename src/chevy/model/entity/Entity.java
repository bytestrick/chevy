package chevy.model.entity;

import chevy.model.entity.EntityTypes;
import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.utilz.Vector2;


public abstract class Entity {
    private final StaticEntityTypes type;
    protected final Vector2<Integer> velocity;


    public Entity(Vector2<Integer> initVelocity, StaticEntityTypes type) {
        this.velocity = initVelocity;
        this.type = type;
    }


    public EntityTypes getSpecificType() { return type; }

    public EntityTypes getGenericType() { return null; }


    @Override
    public String toString() {
        return "Entity";
    }
}
