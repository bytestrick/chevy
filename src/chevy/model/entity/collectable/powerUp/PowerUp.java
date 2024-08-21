package chevy.model.entity.collectable.powerUp;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Utils;
import chevy.utils.Vector2;

import java.util.Random;

public abstract class PowerUp extends Collectable {
    private final Type type;
    private final GlobalState idle = new GlobalState(State.IDLE, 0.5f);
    private final GlobalState selected = new GlobalState(State.SELECTED, 0.2f);
    private final GlobalState deselected = new GlobalState(State.DESELECTED, 0.2f);
    private final GlobalState collected = new GlobalState(State.COLLECTED, 0.8f);
    protected String name = "No name";
    protected String description = "No description";
    protected int occurringPercentage = 0;
    protected int inStock = -1; // quantità infinita


    public PowerUp(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Collectable.Type.POWER_UP);
        this.type = type;

        initStaticMachine();
    }

    public static PowerUp getPowerUp(Vector2<Integer> position) {
        return switch (Type.getRandom()) {
            case HOLY_SHIELD -> new HolyShield(position);
            case VAMPIRE_FANGS -> new VampireFangs(position);
            case ANGEL_RING -> new AngelRing(position);
            case LONG_SWORD -> new LongSword(position);
            case HOBNAIL_BOOTS -> new HobnailBoots(position);
            case COIN_OF_GREED -> new CoinOfGreed(position);
            case HOT_HEART -> new HotHeart(position);
            case COLD_HEART -> new ColdHeart(position);
            case STONE_BOOTS -> new StoneBoots(position);
            case BROKEN_ARROWS -> new BrokenArrows(position);
            case AGILITY -> new Agility(position);
            case HEDGEHOG_SPINES -> new HedgehogSpines(position);
            case SLIME_PIECE -> new SlimePiece(position);
            case PIECE_OF_BONE -> new PieceOfBone(position);
            case GOLD_ARROW -> new GoldArrow(position);
            case HEALING_FLOOD -> new HealingFlood(position);
            case KEY_S_KEEPER -> new KeySKeeper(position);

        };
    }

    private void initStaticMachine() {
//        this.stateMachine.setStateMachineName("PowerUp");
        this.stateMachine.setInitialState(idle);

        idle.linkState(selected);
        idle.linkState(collected);
        selected.linkState(collected);
        selected.linkState(deselected);
        deselected.linkState(idle);
        deselected.linkState(selected);
        deselected.linkState(collected);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GlobalState getState(CommonState commState) {
        State powerUpState = (State) commState;
        return switch (powerUpState) {
            case IDLE -> idle;
            case SELECTED -> selected;
            case COLLECTED -> collected;
            case DESELECTED -> deselected;
        };
    }

    private boolean isOutOfStock() {
        return inStock != 0;
    }

    public boolean canUse() {
        boolean use = isOutOfStock() && Utils.isOccurring(occurringPercentage);
        if (use && inStock > 0) {
            inStock -= 1;
        }
        return use;
    }

    @Override
    public CommonEntityType getSpecificType() {
        return type;
    }

    @Override
    public CommonEntityType getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() { return type.toString(); }

    public enum Type implements CommonEntityType {
        HOLY_SHIELD, VAMPIRE_FANGS, ANGEL_RING, LONG_SWORD, HOBNAIL_BOOTS,
        COIN_OF_GREED, HOT_HEART, COLD_HEART, STONE_BOOTS, BROKEN_ARROWS, AGILITY, HEDGEHOG_SPINES,
        SLIME_PIECE, PIECE_OF_BONE, GOLD_ARROW, HEALING_FLOOD, KEY_S_KEEPER;


        public static Type getRandom() {
            Type[] types = values();
            Random random = new Random();
            return types[random.nextInt(types.length)];
        }
    }

    public enum State implements CommonState {
        IDLE, SELECTED, DESELECTED, COLLECTED
    }
}