package chevy.model.entity.collectable.powerUp;

import chevy.model.entity.EntityType;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Utils;

import java.awt.Point;

public abstract class PowerUp extends Collectable {
    private final Type type;
    private final Vertex idle = new Vertex(State.IDLE, 0.5f);
    private final Vertex selected = new Vertex(State.SELECTED, 0.2f);
    private final Vertex deselected = new Vertex(State.DESELECTED, 0.2f);
    private final Vertex collected = new Vertex(State.COLLECTED, 0.8f);
    String name = "No name";
    String description = "No description";
    int occurringPercentage;
    int inStock = -1; // quantità infinita

    PowerUp(Point position, Type type) {
        super(position, Collectable.Type.POWER_UP);
        this.type = type;

        initStaticMachine();
    }

    public static PowerUp getPowerUp(Point position) {
        return switch (Type.getRandom()) {
            case AGILITY -> new Agility(position);
            case ANGEL_RING -> new AngelRing(position);
            case BROKEN_ARROWS -> new BrokenArrows(position);
            case COIN_OF_GREED -> new CoinOfGreed(position);
            case COLD_HEART -> new ColdHeart(position);
            case GOLD_ARROW -> new GoldArrow(position);
            case HEALING_FLOOD -> new HealingFlood(position);
            case HEDGEHOG_SPINES -> new HedgehogSpines(position);
            case HOBNAIL_BOOTS -> new HobnailBoots(position);
            case HOLY_SHIELD -> new HolyShield(position);
            case HOT_HEART -> new HotHeart(position);
            case KEY_S_KEEPER -> new KeySKeeper(position);
            case LONG_SWORD -> new LongSword(position);
            case SLIME_PIECE -> new SlimePiece(position);
            case STONE_BOOTS -> new StoneBoots(position);
            case VAMPIRE_FANGS -> new VampireFangs(position);
        };
    }

    private void initStaticMachine() {
        stateMachine.setName("PowerUp");
        stateMachine.setInitialState(idle);

        idle.linkVertex(selected);
        idle.linkVertex(collected);
        selected.linkVertex(collected);
        selected.linkVertex(deselected);
        deselected.linkVertex(idle);
        deselected.linkVertex(selected);
        deselected.linkVertex(collected);
    }

    public String getName() {return name;}

    public String getDescription() {return description;}

    public Vertex getState(EntityState state) {
        State powerUpState = (State) state;
        return switch (powerUpState) {
            case IDLE -> idle;
            case SELECTED -> selected;
            case COLLECTED -> collected;
            case DESELECTED -> deselected;
        };
    }

    public boolean canUse() {
        boolean use = inStock != 0 && Utils.isOccurring(occurringPercentage);
        if (use && inStock > 0) {
            inStock -= 1;
        }
        return use;
    }

    @Override
    public EntityType getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return type.toString();}

    public enum Type implements EntityType {
        HOLY_SHIELD, VAMPIRE_FANGS, ANGEL_RING, LONG_SWORD, HOBNAIL_BOOTS, COIN_OF_GREED,
        HOT_HEART, COLD_HEART,
        STONE_BOOTS, BROKEN_ARROWS, AGILITY, HEDGEHOG_SPINES, SLIME_PIECE, GOLD_ARROW,
        HEALING_FLOOD, KEY_S_KEEPER;

        static Type getRandom() {
            Type[] types = values();
            return types[Utils.random.nextInt(types.length)];
        }
    }

    public enum State implements EntityState {IDLE, SELECTED, DESELECTED, COLLECTED}
}