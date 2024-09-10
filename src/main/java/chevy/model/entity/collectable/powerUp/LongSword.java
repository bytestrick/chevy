package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class LongSword extends PowerUp {
    private static final int DAMAGE_BOOST = 2;

    public LongSword(Point position) {
        super(position, Type.LONG_SWORD);

        inStock = 1;
        occurringPercentage = 100;
        name = "Spada Lunga\n";
        description = "Incrementa il danno di spada di " + DAMAGE_BOOST + "\nunità";
    }

    public static int getDamageBoost() {return DAMAGE_BOOST;}
}