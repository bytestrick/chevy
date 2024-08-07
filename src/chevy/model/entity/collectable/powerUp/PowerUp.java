package chevy.model.entity.collectable.powerUp;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
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

    public PowerUp(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Collectable.Type.POWER_UP);
        this.type = type;

        initStaticMachine();
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

    public boolean isOccurring() {
        return new Random().nextInt(1, 100) < occurringPercentage;
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
        THUNDERBOLT, HOLY_SHIELD, VAMPIRE_FANGS, ANGEL_RING, SWIFT_BOOTS, LONG_SWORD, GOD_S_ICE, IGNITION,
        COIN_OF_GREED, HOT_HEART, COLD_HEART, GORGON_S_POISON, STONE_BOOTS, BROKEN_ARROWS, AGILITY, HEDGEHOG_SPINES,
        CAT_CLAW, SLIME_PIECE, PIECE_OF_BONE, GOLD_ARROW;

        public Type getRandom() {
            Type[] types = values();
            Random random = new Random();
            return types[random.nextInt(types.length)];
        }
    }

    public enum State implements CommonState {
        IDLE, SELECTED, DESELECTED, COLLECTED
    }
}