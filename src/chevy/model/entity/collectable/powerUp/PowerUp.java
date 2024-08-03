package chevy.model.entity.collectable.powerUp;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

import java.util.Random;

public abstract class PowerUp extends Collectable {
    public enum Type implements EntityCommonEnumTypes {
        THUNDERBOLT,
        HOLY_SHIELD,
        VAMPIRE_FANGS,
        ANGEL_RING,
        SWIFT_BOOTS,
        LONG_SWORD,
        GOD_S_ICE,
        IGNITION,
        COIN_OF_GREED,
        HOT_HEART,
        COLD_HEART,
        GORGON_S_POISON,
        STONE_BOOTS,
        BROKEN_ARROWS,
        AGILITY,
        HEDGEHOG_SPINES,
        CAT_CLAW,
        SLIME_PIECE,
        PIECE_OF_BONE,
        GOLD_ARROW;

        public Type getRandom() {
            Type[] types = values();
            Random random = new Random();
            return types[random.nextInt(types.length)];
        }
    }
    private final Type type;
    public enum EnumState implements CommonEnumStates {
        IDLE,
        SELECTED,
        DESELECTED,
        COLLECTED
    }
    private final State idle = new State(EnumState.IDLE, 0.5f);
    private final State selected = new State(EnumState.SELECTED, 0.2f);
    private final State deselected = new State(EnumState.DESELECTED, 0.2f);
    private final State collected = new State(EnumState.COLLECTED, 0.8f);
    protected String name = "No name";
    protected String description = "No description";
    protected int occurringPercentage = 0;


    public PowerUp(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Collectable.Type.POWER_UP);
        this.type = type;

        initStaticMachine();
    }


    private void initStaticMachine() {
//        this.stateMachine.setStateMachineName("PowerUp");
        this.stateMachine.setInitialState(idle);

        idle.linkState(selected);
        selected.linkState(collected);
        selected.linkState(deselected);
        deselected.linkState(idle);
        deselected.linkState(selected);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public State getState(CommonEnumStates commonEnumStates) {
        EnumState powerUpEnumState = (EnumState) commonEnumStates;
        return switch (powerUpEnumState) {
            case IDLE -> idle;
            case SELECTED -> selected;
            case COLLECTED -> collected;
            case DESELECTED -> deselected;
        };
    }

    public boolean isOccurring() {
        return new Random().nextInt(1, 100) < occurringPercentage;
    }


    @Override
    public EntityCommonEnumTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonEnumTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() { return type.toString(); }
}
