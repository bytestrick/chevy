package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class BrokenArrows extends PowerUp {
    public BrokenArrows(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.BROKEN_ARROWS);

        occurringPercentage = 100;
        name = "Spezza Frecce\n";
        description = "Le frecce non ti infliggeranno danno";
    }
}
