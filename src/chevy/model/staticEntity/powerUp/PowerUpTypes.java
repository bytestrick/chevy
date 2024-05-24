package chevy.model.staticEntity.powerUp;

import java.util.Random;

public enum PowerUpTypes {
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

    public PowerUpTypes getRandom() {
        PowerUpTypes[] types = values();
        Random random = new Random();
        return types[random.nextInt(types.length)];
    }
}
