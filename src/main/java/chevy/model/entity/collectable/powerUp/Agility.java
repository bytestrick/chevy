package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class Agility extends PowerUp {
    public Agility(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.AGILITY);

        occurringPercentage = 20;
        name = "Agilità\n";
        description = "Possibilità del " + occurringPercentage + "% di schivare un attacco";
    }
}