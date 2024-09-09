package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class StoneBoots extends PowerUp {
    public StoneBoots(Point initVelocity) {
        super(initVelocity, Type.STONE_BOOTS);

        occurringPercentage = 100;
        name = "Stivali di pietra\n";
        description = "Le trappole chiodate non hanno effetto";
    }
}