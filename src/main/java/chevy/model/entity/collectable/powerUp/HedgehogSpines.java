package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.Point;

public final class HedgehogSpines extends PowerUp {
    private static final float DAMAGE_MULTIPLIER = .2f;

    public HedgehogSpines(Point position) {
        super(position, Type.HEDGEHOG_SPINES);

        occurringPercentage = 50;
    }

    public static float getDamageMultiplier() {return DAMAGE_MULTIPLIER;}

    @Override
    public String getName() {return Options.strings.getString("powerUp.hedgehogSpines.name");}

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.hedgehogSpines.desc"),
                occurringPercentage, Math.round(DAMAGE_MULTIPLIER * 100));
    }
}