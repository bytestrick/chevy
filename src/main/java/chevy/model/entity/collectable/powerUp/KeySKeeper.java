package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class KeySKeeper extends PowerUp {
    private final float increaseDropPercentage = .3f;

    public KeySKeeper(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.KEY_S_KEEPER);

        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Guardiano delle chiavi\n";
        this.description =
                "I nemici aumentano la probabilità di\nrilasciare chiavi del " + (int) (increaseDropPercentage * 100) + "%";
    }

    public float getIncreaseDropPercentage() {
        return increaseDropPercentage;
    }
}
