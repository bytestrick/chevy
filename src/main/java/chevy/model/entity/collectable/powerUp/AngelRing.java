package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class AngelRing extends PowerUp {
    public AngelRing(Point position) {
        super(position, Type.ANGEL_RING);

        inStock = 1;
        occurringPercentage = 100;
        name = "Anello d'Angelo\n";
        description = "Il giocatore viene riportato in vita\n quando muore. Effetto monouso";
    }
}