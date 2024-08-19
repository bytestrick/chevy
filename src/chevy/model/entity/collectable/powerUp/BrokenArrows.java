package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class BrokenArrows extends PowerUp {
    public BrokenArrows(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.BROKEN_ARROWS);

        this.occurringPercentage = 100;
        this.name = "Spezza Frecce\n";
        this.description = "Le frecce non ti infliggeranno danno";
    }
}
