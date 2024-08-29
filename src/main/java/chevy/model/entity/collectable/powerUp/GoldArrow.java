package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class GoldArrow extends PowerUp {
    private final int increaseDamage = 2;

    public GoldArrow(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.GOLD_ARROW);

        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Frecce Dorate\n";
        this.description = "Aumenta il danno delle frecce di +" + increaseDamage;
    }

    public int getIncreaseDamage() {
        return increaseDamage;
    }
}
