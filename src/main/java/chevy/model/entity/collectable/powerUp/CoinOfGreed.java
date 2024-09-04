package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class CoinOfGreed extends PowerUp {
    private final float increaseDropPercentage = .3f;

    public CoinOfGreed(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.COIN_OF_GREED);

        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Avido di Monete\n";
        this.description =
                "I nemici aumentano la probabilità di\nrilasciare monete del " + (int) (increaseDropPercentage * 100) + "%";
    }

    public float getIncreaseDropPercentage() {
        return increaseDropPercentage;
    }
}
