package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.EntityType;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.stateMachine.EntityState;

import java.awt.Point;

public abstract class Projectile extends DynamicEntity {
    private final Type type;
    private final Direction direction;
    private boolean collision;

    Projectile(Point position, Type type, Direction direction) {
        super(position, DynamicEntity.Type.PROJECTILE);
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
