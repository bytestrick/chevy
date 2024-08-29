package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class AngelRing extends PowerUp {
    public AngelRing(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.ANGEL_RING);

        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Anello d'Angelo\n";
        this.description = "Il giocatore viene riportato in vita\n quando muore. Effetto monouso";
    }
}
