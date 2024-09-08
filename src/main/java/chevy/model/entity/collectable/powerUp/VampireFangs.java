package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class VampireFangs extends PowerUp {
    private static final int HEALTH_BOOST = 1;

    public VampireFangs(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.VAMPIRE_FANGS);

        occurringPercentage = 5;
        name = "Zanne di Vampiro\n";
        description = "Possibilità del " + occurringPercentage + "% di recuperare\n" + HEALTH_BOOST + " unità "
                + "di vita durante un attacco";
    }

    public static int getHealthBoost() { return HEALTH_BOOST; }
}
