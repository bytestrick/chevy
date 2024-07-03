package chevy.model.entity.dinamicEntity.projectile;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.utils.Vector2;

public abstract class Projectile extends DynamicEntity {
    private final Type type;
    private final DirectionsModel direction;
    private boolean collision = false;

    public Projectile(Vector2<Integer> initPosition, Type type, DirectionsModel direction, float advanceTimer) {
        super(initPosition, DynamicEntity.Type.PROJECTILE);

        this.type = type;
        this.direction = direction;
        this.crossable = true;
        this.safeToCross = false;
        this.layer = 1;
    }

    public boolean isCollision() { return collision; }

    public void setCollision(boolean collision) { this.collision = collision; }

    public DirectionsModel getDirection() { return direction; }

    @Override
    public EntityCommonEnumTypes getSpecificType() { return type; }

    @Override
    public EntityCommonEnumTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() { return "PROJECTILE"; }

    public enum Type implements EntityCommonEnumTypes {
        ARROW, FIRE_BALL, SLIME_SHOT;
    }

    public enum States implements CommonEnumStates {
        START, LOOP, END;
    }
}