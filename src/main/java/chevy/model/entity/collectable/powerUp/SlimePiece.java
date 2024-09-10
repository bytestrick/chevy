package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

public final class SlimePiece extends PowerUp {
    private static final int NUMBER_OF_SLIMES = 1;
    private static final int DAMAGE_BOOST = 4;

    public SlimePiece(Point position) {
        super(position, Type.SLIME_PIECE);

        occurringPercentage = 100;
        name = "Pezzo di Slime\n";
        description = "Il danno d'attacco aumenta\ndi " + DAMAGE_BOOST
                + ", ma dopo la morte i nemici\ngenerano " + NUMBER_OF_SLIMES
                + " piccoli slime.\nI piccoli slime non generano\naltri slime";
    }

    public static int getNumberOfSlimes() {return NUMBER_OF_SLIMES;}

    public static int getDamageBoost() {return DAMAGE_BOOST;}
}