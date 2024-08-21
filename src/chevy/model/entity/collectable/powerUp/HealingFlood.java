package chevy.model.entity.collectable.powerUp;
import chevy.utils.Vector2;

public class HealingFlood extends PowerUp {
    private final float increaseDropPercentage = .3f;

    public HealingFlood(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.HEALING_FLOOD);

        // Healing Flood: I nemici aumentano la probabilità di rilasciare pozioni di cura del 15%.
        this.inStock = 1;
        this.occurringPercentage = 100;
        this.name = "Inondazione Curativa\n";
        this.description = "I nemici aumentano la probabilità di\nrilasciare pozioni di cura del " + (int) (increaseDropPercentage * 100) + "%";
    }

    public float getIncreaseDropPercentage() {
        return increaseDropPercentage;
    }
}
