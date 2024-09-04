package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class LongSword extends PowerUp {
    private final int increaseDamage = 2;

    public LongSword(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.LONG_SWORD);

        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Spada Lunga\n";
        this.description = "Incrementa il danno di spada di " + increaseDamage + "\nunità";
    }

    public int getIncreaseDamage() {
        return increaseDamage;
    }
}
