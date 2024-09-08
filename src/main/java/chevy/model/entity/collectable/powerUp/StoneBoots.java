package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class StoneBoots extends PowerUp {
    public StoneBoots(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.STONE_BOOTS);

        occurringPercentage = 100;
        name = "Stivali di pietra\n";
        description = "Le trappole chiodate non hanno effetto";
    }
}