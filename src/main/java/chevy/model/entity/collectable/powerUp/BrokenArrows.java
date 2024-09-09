package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class BrokenArrows extends PowerUp {
    public BrokenArrows(Point initVelocity) {
        super(initVelocity, Type.BROKEN_ARROWS);

        occurringPercentage = 100;
        name = "Spezza Frecce\n";
        description = "Le frecce non ti infliggeranno danno";
    }
}
