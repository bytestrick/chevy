package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.Point;

public final class LongSword extends PowerUp {
    private static final int DAMAGE_BOOST = 2;

    public LongSword(Point position) {
        super(position, Type.LONG_SWORD);

        inStock = 1;
        occurringPercentage = 100;
    }

    public static int getDamageBoost() {return DAMAGE_BOOST;}

    @Override
    public String getName() {return Options.strings.getString("powerUp.longSword.name");}

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.longSword.desc"), DAMAGE_BOOST);
    }
}