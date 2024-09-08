package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.EntityType;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.stateMachine.EntityState;
import chevy.utils.Vector2;

public abstract class Projectile extends DynamicEntity {
    private final Type type;
    private final Direction direction;
    private boolean collision;

    public Projectile(Vector2<Integer> initPosition, Type type, Direction direction) {
        super(initPosition, DynamicEntity.Type.PROJECTILE);
        this.type = type;
        this.direction = direction;
        crossable = true;
        safeToCross = false;
        drawLayer = 4;
    }

    public boolean isCollision() {return collision;}

    public void setCollision(boolean collision) {this.collision = collision;}

    public Direction getDirection() {return direction;}

    @Override
    public EntityType getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return "PROJECTILE";}

    public enum Type implements EntityType {ARROW, SLIME_SHOT}

    public enum State implements EntityState {START, LOOP, END}
}