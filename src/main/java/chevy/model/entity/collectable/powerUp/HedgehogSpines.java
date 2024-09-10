package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class HedgehogSpines extends PowerUp {
    private static final float DAMAGE_MULTIPLIER = .2f;

    public HedgehogSpines(Point position) {
        super(position, Type.HEDGEHOG_SPINES);

        occurringPercentage = 50;
        name = "Aculei di Riccio\n";
        description = "Probabilità del " + occurringPercentage + "% di riflettere\nil "
                + Math.round(DAMAGE_MULTIPLIER * 100) + "% di danno al tuo aguzzino";
    }

    public static float getDamageMultiplier() {return DAMAGE_MULTIPLIER;}
}