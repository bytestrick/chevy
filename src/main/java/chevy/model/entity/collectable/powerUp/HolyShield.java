package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class HolyShield extends PowerUp {
    private final float reduceDamage = 0.5f; // valore tra 0 e 1

    public HolyShield(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.HOLY_SHIELD);

        this.occurringPercentage = 100;
        this.name = "Scudo Sacro\n";
        this.description = "Riduce i danni in arrivo del " + (int) (reduceDamage * 100) + "%";
    }

    public float getReduceDamage() {
        return reduceDamage;
    }
}
