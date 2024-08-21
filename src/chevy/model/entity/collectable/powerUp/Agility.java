package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class Agility extends PowerUp {
    public Agility(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.AGILITY);

        this.occurringPercentage = 20;
        this.name = "Agilità\n";
        this.description = "Possibilità del " + occurringPercentage + "% di schivare un attacco";
    }
}
