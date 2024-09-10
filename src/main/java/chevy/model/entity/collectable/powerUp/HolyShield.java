package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class HolyShield extends PowerUp {
    private static final float DAMAGE_REDUCTION_MULTIPLIER = 0.5f; // valore tra 0 e 1

    public HolyShield(Point position) {
        super(position, Type.HOLY_SHIELD);

        occurringPercentage = 100;
        name = "Scudo Sacro\n";
        description = "Riduce i danni in arrivo del "
                + Math.round(DAMAGE_REDUCTION_MULTIPLIER * 100) + "%";
    }

    public static float getDamageReductionMultiplier() {return DAMAGE_REDUCTION_MULTIPLIER;}
}