package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class LongSword extends PowerUp {
    private static final int DAMAGE_BOOST = 2;

    public LongSword(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.LONG_SWORD);

        inStock = 1;
        occurringPercentage = 100;
        name = "Spada Lunga\n";
        description = "Incrementa il danno di spada di " + DAMAGE_BOOST + "\nunità";
    }

    public static int getDamageBoost() {return DAMAGE_BOOST;}
}