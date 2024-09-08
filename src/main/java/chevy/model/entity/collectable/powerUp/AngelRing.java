package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class AngelRing extends PowerUp {
    public AngelRing(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.ANGEL_RING);

        inStock = 1;
        occurringPercentage = 100;
        name = "Anello d'Angelo\n";
        description = "Il giocatore viene riportato in vita\n quando muore. Effetto monouso";
    }
}