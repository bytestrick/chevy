package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class HolyShield extends PowerUp {
    private static final float DAMAGE_REDUCTION_MULTIPLIER = 0.5f; // value between 0 and 1

    public HolyShield(Point position) {
        super(position, Type.HOLY_SHIELD);

        occurringPercentage = 100;
    }

    public static float getDamageReductionMultiplier() {
        return DAMAGE_REDUCTION_MULTIPLIER;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.holyShield.name");
    }

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.holyShield.desc"),
                Math.round(DAMAGE_REDUCTION_MULTIPLIER * 100));
    }
}
