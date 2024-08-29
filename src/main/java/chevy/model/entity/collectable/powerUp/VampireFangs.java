package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class VampireFangs extends PowerUp {
    private final int recoveryHealth = 1;

    public VampireFangs(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.VAMPIRE_FANGS);

        this.occurringPercentage = 5;
        this.name = "Zanne di Vampiro\n";
        this.description = "Possibilità del " + occurringPercentage + "% di recuperare\n" + recoveryHealth + " unità "
                + "di vita durante un attacco";
    }

    public int getRecoveryHealth() {
        return recoveryHealth;
    }
}
