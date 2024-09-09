package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class HobnailBoots extends PowerUp {
    public HobnailBoots(Point initVelocity) {
        super(initVelocity, Type.HOBNAIL_BOOTS);

        occurringPercentage = 100;
        name = "Stivali Chiodati\n";
        description = "Aumenta la presa sul ghiaccio\nimpedendo di scivolarci sopra";
    }
}