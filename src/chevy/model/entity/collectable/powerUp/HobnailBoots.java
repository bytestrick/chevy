package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class HobnailBoots extends PowerUp {
    public HobnailBoots(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.HOBNAIL_BOOTS);

        this.occurringPercentage = 100;
        this.name = "Stivali Chiodati\n";
        this.description = "Aumenta la presa sul ghiaccio\nimpedendo di scivolarci sopra";
    }
}
