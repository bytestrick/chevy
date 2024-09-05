package chevy.model.entity.dynamicEntity.projectile;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;

public abstract class Projectile extends DynamicEntity {
    private final Type type;
    private final Direction direction;
    private boolean collision = false;

    public Projectile(Vector2<Integer> initPosition, Type type, Direction direction) {
        super(initPosition, DynamicEntity.Type.PROJECTILE);

        this.type = type;
        this.direction = direction;
        this.crossable = true;
        this.safeToCross = false;
        this.drawLayer = 4;
    }

    public boolean isCollision() { return collision; }

    public void setCollision(boolean collision) { this.collision = collision; }

    public Direction getDirection() { return direction; }

    @Override
    public CommonEntityType getSpecificType() { return type; }

    @Override
    public CommonEntityType getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() { return "PROJECTILE"; }

    public enum Type implements CommonEntityType {
        ARROW, FIRE_BALL, SLIME_SHOT
    }

    public enum State implements CommonState {
        START, LOOP, END
    }
}