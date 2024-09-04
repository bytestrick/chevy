package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class ColdHeart extends PowerUp {
    private final int increaseShield = 3;

    public ColdHeart(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.COLD_HEART);

        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Cuore di Ghiaccio\n";
        this.description = "Lo scudo avrà un aumento di +" + increaseShield + ".\nEffetto monouso";
    }

    public int getIncreaseShield() {
        return increaseShield;
    }
}
