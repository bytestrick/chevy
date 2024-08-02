package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class Agility extends PowerUp {
    public Agility(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.AGILITY);

        super.name = "Agility";
        super.description = "Descrizione agilità...";
    }
}
