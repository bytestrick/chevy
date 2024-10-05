package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.Point;

public final class GoldArrow extends PowerUp {
    private static final int DAMAGE_BOOST = 2;

    public GoldArrow(Point position) {
        super(position, Type.GOLD_ARROWS);

        inStock = 1;
        occurringPercentage = 100;
    }

    public static int getDamageBoost() {return DAMAGE_BOOST;}

    @Override
    public String getName() {return Options.strings.getString("powerUp.goldArrow.name");}

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.goldArrow.desc"), DAMAGE_BOOST);
    }
}
