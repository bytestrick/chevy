package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class ColdHeart extends PowerUp {
    private static final int SHIELD_BOOST = 3;

    public ColdHeart(Point position) {
        super(position, Type.COLD_HEART);

        inStock = 1;
        occurringPercentage = 100;
        name = "Cuore di Ghiaccio\n";
        description = "Lo scudo avrà un aumento di +" + SHIELD_BOOST + ".\nEffetto monouso";
    }

    public static int getShieldBoost() {return SHIELD_BOOST;}
}