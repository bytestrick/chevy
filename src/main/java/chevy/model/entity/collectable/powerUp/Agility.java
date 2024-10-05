package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.Point;

public final class Agility extends PowerUp {
    public Agility(Point position) {
        super(position, Type.AGILITY);
        occurringPercentage = 20;
    }

    @Override
    public String getName() {return Options.strings.getString("powerUp.agility.name");}

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.agility.desc"),
                occurringPercentage);
    }
}