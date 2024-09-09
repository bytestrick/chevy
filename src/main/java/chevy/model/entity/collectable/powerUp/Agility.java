package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class Agility extends PowerUp {
    public Agility(Point initVelocity) {
        super(initVelocity, Type.AGILITY);

        occurringPercentage = 20;
        name = "Agilità\n";
        description = "Possibilità del " + occurringPercentage + "% di schivare un attacco";
    }
}