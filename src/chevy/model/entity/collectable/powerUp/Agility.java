package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class Agility extends PowerUp {
    public Agility(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.AGILITY);

        this.occurringPercentage = 100;
        this.name = "Agility\n";
        this.description = occurringPercentage + "% chance of avoiding an attack";
    }
}
