package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.*;

public final class SlimePiece extends PowerUp {
    private static final int NUMBER_OF_SLIMES = 1;
    private static final int DAMAGE_BOOST = 4;

    public SlimePiece(Point position) {
        super(position, Type.SLIME_PIECE);

        occurringPercentage = 100;
    }

    public static int getNumberOfSlimes() {
        return NUMBER_OF_SLIMES;
    }

    public static int getDamageBoost() {
        return DAMAGE_BOOST;
    }

    @Override
    public String getName() {
        return Options.strings.getString("powerUp.slimePiece.name");
    }

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp" +
                ".slimePiece.desc"), DAMAGE_BOOST, NUMBER_OF_SLIMES);
    }
}
