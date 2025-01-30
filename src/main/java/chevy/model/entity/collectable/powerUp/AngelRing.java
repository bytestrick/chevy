package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class AngelRing extends PowerUp {
    public AngelRing(Point position) {
        super(position, Type.ANGEL_RING);

        inStock = 1;
        occurringPercentage = 100;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.angelRing.name");
    }

    @Override
    public String getDescription() {
        return Options.strings.getString("powerUp.angelRing.desc");
    }
}
