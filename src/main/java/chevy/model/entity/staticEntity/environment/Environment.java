package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityType;
import chevy.model.entity.stateMachine.EntityState;
import chevy.utils.Vector2;

public abstract class Environment extends Entity {
    private final Type type;

    public Environment(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Entity.Type.ENVIRONMENT);
        this.type = type;
        drawLayer = 1;
    }

    @Override
    public EntityType getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return type.toString();}

    public enum Type implements EntityType {GROUND, WALL, STAIR, TRAP, CHEST}

    public enum State implements EntityState {OPEN}
}
