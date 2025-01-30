package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class HotHeart extends PowerUp {
    private static final int HEALTH_BOOST = 3;

    public HotHeart(Point position) {
        super(position, Type.HOT_HEART);

        inStock = 1;
        occurringPercentage = 100;
    }

    public static int getHealthBoost() {
        return HEALTH_BOOST;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.hotHeart.name");
    }

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.hotHeart.desc"), HEALTH_BOOST);
    }
}
