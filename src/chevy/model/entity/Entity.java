package chevy.model.entity;

import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.utilz.Vector2;


public abstract class Entity {
    private final StaticEntityTypes type;
    protected final Vector2<Integer> position;
    protected boolean crossable = false;


    public Entity(Vector2<Integer> initVelocity, StaticEntityTypes type) {
        this.position = initVelocity;
        this.type = type;
    }


    public EntityTypes getSpecificType() { return type; }

    public EntityTypes getGenericType() { return null; }

    public int getRow() { return position.first(); }

    public int getCol() { return position.second(); }

    public boolean isCrossable() { return crossable; }

    @Override
    public String toString() {
        return "ENTITY";
    }
}
