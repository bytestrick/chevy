package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class BrokenArrows extends PowerUp {
    public BrokenArrows(Point position) {
        super(position, Type.BROKEN_ARROWS);

        occurringPercentage = 100;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.brokenArrows.name");
    }

    @Override
    public String getDescription() {
        return Options.strings.getString("powerUp.brokenArrows.desc");
    }
}
