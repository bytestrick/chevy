package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class ColdHeart extends PowerUp {
    private static final int SHIELD_BOOST = 3;

    public ColdHeart(Point position) {
        super(position, Type.COLD_HEART);

        inStock = 1;
        occurringPercentage = 100;
    }

    public static int getShieldBoost() {
        return SHIELD_BOOST;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.coldHeart.name");
    }

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.coldHeart.desc"), SHIELD_BOOST);
    }
}
