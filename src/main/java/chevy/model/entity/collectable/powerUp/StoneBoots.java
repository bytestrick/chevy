package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class StoneBoots extends PowerUp {
    public StoneBoots(Point position) {
        super(position, Type.STONE_BOOTS);

        occurringPercentage = 100;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.stoneBoots.name");
    }

    @Override
    public String getDescription() {
        return Options.strings.getString("powerUp.stoneBoots.desc");
    }
}
