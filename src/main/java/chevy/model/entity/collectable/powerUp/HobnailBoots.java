package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class HobnailBoots extends PowerUp {
    public HobnailBoots(Point position) {
        super(position, Type.HOBNAIL_BOOTS);

        occurringPercentage = 100;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.hobnailBoots.name");
    }

    @Override
    public String getDescription() {
        return Options.strings.getString("powerUp.hobnailBoots.desc");
    }
}
