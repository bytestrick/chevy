package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class HotHeart extends PowerUp {
    private static final int HEALTH_BOOST = 3;

    public HotHeart(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.HOT_HEART);

        inStock = 1;
        occurringPercentage = 100;
        name = "Cuore di Fuoco\n";
        description = "La vita avrà un aumento di +" + HEALTH_BOOST + ".\nEffetto monouso";
    }

    public static int getHealthBoost() {return HEALTH_BOOST;}
}