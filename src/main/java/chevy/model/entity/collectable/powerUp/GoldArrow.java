package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class GoldArrow extends PowerUp {
    private static final int DAMAGE_BOOST = 2;

    public GoldArrow(Point position) {
        super(position, Type.GOLD_ARROW);

        inStock = 1;
        occurringPercentage = 100;
        name = "Frecce Dorate\n";
        description = "Aumenta il danno delle frecce di +" + DAMAGE_BOOST;
    }

    public static int getDamageBoost() {return DAMAGE_BOOST;}
}
