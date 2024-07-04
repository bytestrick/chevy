package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.utils.Vector2;

public abstract class Player extends LiveEntity {
    private final Type type;
    protected float speed = 0.1f;

    public Player(Vector2<Integer> initPosition, Type type) {
        super(initPosition, LiveEntity.Type.PLAYER);
        this.type = type;

        this.layer = 2;
    }

    @Override
    public Type getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonEnumTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public enum States implements CommonStates {
        IDLE, ATTACK, MOVE, DEAD, HIT, SLUDGE, FALL, GLIDE
    }

    public enum Type implements EntityCommonEnumTypes {
        KNIGHT, NINJA, ARCHER;
    }
}