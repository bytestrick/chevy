package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class KeySKeeper extends PowerUp {
    private static final float INCREASED_DROP_PROBABILITY = .3f;

    public KeySKeeper(Point position) {
        super(position, Type.KEY_S_KEEPER);

        inStock = 1;
        occurringPercentage = 100;
    }

    public static float getIncreasedDropProbability() {
        return INCREASED_DROP_PROBABILITY;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.keySKeeper.name");
    }

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.keySKeeper.desc"),
                Math.round(INCREASED_DROP_PROBABILITY * 100));
    }
}
