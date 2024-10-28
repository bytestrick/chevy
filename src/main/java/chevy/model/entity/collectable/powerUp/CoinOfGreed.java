package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.Point;

public final class CoinOfGreed extends PowerUp {
    private static final float INCREASED_DROP_PROBABILITY = .3f;

    public CoinOfGreed(Point position) {
        super(position, Type.COIN_OF_GREED);

        inStock = 1;
        occurringPercentage = 100;
    }

    public static float getIncreasedDropProbability() {return INCREASED_DROP_PROBABILITY;}

    @Override
    public String getName() {return Options.strings.getString("powerUp.coinOfGreed.name");}

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.coinOfGreed.desc"),
                Math.round(INCREASED_DROP_PROBABILITY * 100));
    }
}
