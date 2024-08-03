package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.utils.Vector2;

import java.util.HashMap;
import java.util.Map;

public abstract class Player extends LiveEntity {
    public enum EnumState implements CommonEnumStates {
        IDLE,
        ATTACK,
        MOVE,
        DEAD,
        HIT,
        SLUDGE,
        FALL,
        GLIDE
    }
    public enum Type implements EntityCommonEnumTypes {
        KNIGHT,
        NINJA,
        ARCHER;
    }
    private final Type type;
    protected float speed = 0.1f;
    protected Map<PowerUp.Type, PowerUp> ownedPowerUp = new HashMap<>();


    public Player(Vector2<Integer> initPosition, Type type) {
        super(initPosition, LiveEntity.Type.PLAYER);
        this.type = type;

        this.drawLayer = 2;
    }


    public void acquirePowerUp(PowerUp.Type powerUpType, PowerUp powerUp) {
        if (!ownedPowerUp.containsKey(powerUpType)) {
            ownedPowerUp.put(powerUpType, powerUp);
            System.out.println(this + " acquire the power up " + powerUpType);
        }
    }

    public PowerUp getOwnedPowerUp(PowerUp.Type powerUpType) {
        return ownedPowerUp.get(powerUpType);
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
}
