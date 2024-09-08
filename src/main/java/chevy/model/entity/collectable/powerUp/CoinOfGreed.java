package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class CoinOfGreed extends PowerUp {
    private static final float INCREASED_DROP_PROBABILITY = .3f;

    public CoinOfGreed(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.COIN_OF_GREED);

        inStock = 1;
        occurringPercentage = 100;
        name = "Avido di Monete\n";
        description = "I nemici aumentano la probabilità di\nrilasciare monete del "
                + Math.round(INCREASED_DROP_PROBABILITY * 100) + "%";
    }

    public static float getIncreasedDropProbability() {return INCREASED_DROP_PROBABILITY;}
}