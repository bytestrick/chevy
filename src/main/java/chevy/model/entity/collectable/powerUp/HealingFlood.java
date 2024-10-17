package chevy.model.entity.collectable.powerUp;

import chevy.view.Options;

import java.awt.Point;

/**
 * I nemici aumentano la probabilità di rilasciare pozioni di cura del 30%
 */
public final class HealingFlood extends PowerUp {
    private static final float INCREASED_DROP_PROBABILITY = .3f;

    public HealingFlood(Point position) {
        super(position, Type.HEALING_FLOOD);

        inStock = 1;
        occurringPercentage = 100;
    }

    public static float getIncreasedDropProbability() {return INCREASED_DROP_PROBABILITY;}

    @Override
    public String getName() {return Options.strings.getString("powerUp.healingFlood.name");}

    @Override
    public String getDescription() {
        return String.format(Options.strings.getString("powerUp.healingFlood.desc"),
                Math.round(INCREASED_DROP_PROBABILITY * 100));
    }
}