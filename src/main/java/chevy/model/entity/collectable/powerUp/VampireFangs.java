package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class VampireFangs extends PowerUp {
    private static final int HEALTH_BOOST = 1;

    public VampireFangs(Point position) {
        super(position, Type.VAMPIRE_FANGS);

        occurringPercentage = 5;
    }

    public static int getHealthBoost() {
        return HEALTH_BOOST;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.vampireFangs.name");
    }

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.vampireFangs.desc"),
                occurringPercentage, HEALTH_BOOST);
    }
}
