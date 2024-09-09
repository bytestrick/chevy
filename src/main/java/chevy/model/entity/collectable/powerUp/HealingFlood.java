package chevy.model.entity.collectable.powerUp;

import java.awt.Point;

/**
 * I nemici aumentano la probabilità di rilasciare pozioni di cura del 30%
 */
public final class HealingFlood extends PowerUp {
    private static final float INCREASED_DROP_PROBABILITY = .3f;

    public HealingFlood(Point initVelocity) {
        super(initVelocity, Type.HEALING_FLOOD);

        inStock = 1;
        occurringPercentage = 100;
        name = "Inondazione Curativa\n";
        description = "I nemici aumentano la probabilità di\nrilasciare pozioni di cura del "
                + Math.round(INCREASED_DROP_PROBABILITY * 100) + "%";
    }

    public static float getIncreasedDropProbability() {return INCREASED_DROP_PROBABILITY;}
}