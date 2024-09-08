package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class HobnailBoots extends PowerUp {
    public HobnailBoots(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.HOBNAIL_BOOTS);

        occurringPercentage = 100;
        name = "Stivali Chiodati\n";
        description = "Aumenta la presa sul ghiaccio\nimpedendo di scivolarci sopra";
    }
}