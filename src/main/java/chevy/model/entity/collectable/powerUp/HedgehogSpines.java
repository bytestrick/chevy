package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public class HedgehogSpines extends PowerUp {
    private final float damagePercentage = 0.2f;
    public HedgehogSpines(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.HEDGEHOG_SPINES);

        this.occurringPercentage = 50;
        this.name = "Aculei di Riccio\n";
        this.description = "Probabilità del "+ occurringPercentage +"% di riflettere\nil " + (int) (damagePercentage * 100) + "% di danno al tuo aguzzino";
    }

    public float getDamagePercentage() {
        return damagePercentage;
    }
}
