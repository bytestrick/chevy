package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class StoneBoots extends PowerUp {
    public StoneBoots(Point position) {
        super(position, Type.STONE_BOOTS);

        occurringPercentage = 100;
        name = "Stivali di pietra\n";
        description = "Le trappole chiodate non hanno effetto";
    }
}