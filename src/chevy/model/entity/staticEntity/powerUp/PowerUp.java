package chevy.model.entity.staticEntity.powerUp;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityCommonEnumTypes;
import chevy.utilz.Vector2;

import java.util.Random;

public abstract class PowerUp extends Entity {
    public enum Type implements EntityCommonEnumTypes {
        THUNDERBOLT,
        HOLY_SHIELD,
        VAMPIRE_FANGS,
        ANGEL_RING,
        SWIFT_BOOTS,
        LONG_SWORD,
        GOD_S_ICE,
        IGNITION,
        COIN_OF_GREED,
        HOT_HEART,
        COLD_HEART,
        GORGON_S_POISON,
        STONE_BOOTS,
        BROKEN_ARROWS,
        AGILITY,
        HEDGEHOG_SPINES,
        CAT_CLAW,
        SLIME_PIECE,
        PIECE_OF_BONE,
        MILK,
        CARROT,
        GOLD_ARROW;

        public Type getRandom() {
            Type[] types = values();
            Random random = new Random();
            return types[random.nextInt(types.length)];
        }
    }
    private final Type type;

    public PowerUp(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Entity.Type.POWER_UP);
        this.type = type;
        this.crossable = true;
    }

    @Override
    public EntityCommonEnumTypes getSpecificType() {
        return type;
    }

    @Override
    public String toString() { return type.toString(); }
}
