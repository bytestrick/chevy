package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class HotHeart extends PowerUp {
    private final int increaseHealth = 3;

    public HotHeart(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.HOT_HEART);

        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Cuore di Fuoco\n";
        this.description = "La vita avrà un aumento di +" + increaseHealth + ".\nEffetto monouso";
    }

    public int getIncreaseHealth() {
        return increaseHealth;
    }
}
