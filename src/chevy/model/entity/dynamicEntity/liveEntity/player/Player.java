package chevy.model.entity.dynamicEntity.liveEntity.player;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Log;
import chevy.utils.Vector2;

import java.util.HashMap;
import java.util.Map;

public abstract class Player extends LiveEntity {
    private final Type type;
    protected float speed = 0f;
    protected Map<PowerUp.Type, PowerUp> ownedPowerUp = new HashMap<>();

    public Player(Vector2<Integer> initPosition, Type type) {
        super(initPosition, LiveEntity.Type.PLAYER);
        this.type = type;

        this.drawLayer = 2;
    }

    public void acquirePowerUp(PowerUp.Type powerUpType, PowerUp powerUp) {
        if (!ownedPowerUp.containsKey(powerUpType)) {
            ownedPowerUp.put(powerUpType, powerUp);
            Log.info(this + " acquire the power up " + powerUpType);
        }
    }

    public int[] getStats() { return new int[]{health * 10, maxDamage * 10, 100 - (int)(speed * 100)}; }

    public PowerUp getOwnedPowerUp(PowerUp.Type powerUpType) {
        return ownedPowerUp.get(powerUpType);
    }

    @Override
    public Type getSpecificType() {
        return type;
    }

    @Override
    public CommonEntityType getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }

    public enum State implements CommonState {
        IDLE, ATTACK, MOVE, DEAD, HIT, SLUDGE, FALL, GLIDE
    }

    public enum Type implements CommonEntityType {
        KNIGHT, ARCHER, NINJA
    }
}