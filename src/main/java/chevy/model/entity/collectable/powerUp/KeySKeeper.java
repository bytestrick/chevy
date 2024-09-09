package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class KeySKeeper extends PowerUp {
    private static final float INCREASED_DROP_PROBABILITY = .3f;

    public KeySKeeper(Point initVelocity) {
        super(initVelocity, Type.KEY_S_KEEPER);

        inStock = 1;
        occurringPercentage = 100;
        name = "Guardiano delle chiavi\n";
        description = "I nemici aumentano la probabilità di\nrilasciare chiavi del "
                + Math.round(INCREASED_DROP_PROBABILITY * 100) + "%";
    }

    public static float getIncreasedDropProbability() {return INCREASED_DROP_PROBABILITY;}
}