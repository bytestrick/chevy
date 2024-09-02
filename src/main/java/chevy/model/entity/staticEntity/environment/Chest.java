package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Utils;
import chevy.utils.Vector2;

public class Chest extends Environment {

    /**
     * Oggetti collezionabili che i nemici possono rilasciare
     */
    private static final Collectable.Type[] DROPPABLE_COLLECTABLE = {Collectable.Type.COIN, Collectable.Type.KEY,
            Collectable.Type.HEALTH, Collectable.Type.POWER_UP};
    /**
     * Probabilità di rilascio degli oggetti collezionabili
     */
    private static final int[] DROPPABLE_COLLECTABLE_PROB = {100, 15, 15, 35};
    private final GlobalState idleLocked = new GlobalState(State.IDLE_LOCKED);
    private final GlobalState idleUnlocked = new GlobalState(State.IDLE_UNLOCKED);
    private final GlobalState open = new GlobalState(State.OPEN, 0.8f);
    private final GlobalState unlock = new GlobalState(State.UNLOCK, 0.3f);
    private final GlobalState close = new GlobalState(State.CLOSE, 0.8f);
    private final int maxDrop = 6;
    private final int minDrop = 3;
    private boolean onePowerUp = false;
    private boolean isFirstOpen = true;

    public Chest(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.CHEST);

        this.mustBeUpdate = true;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Chest");
        stateMachine.setInitialState(idleLocked);

        idleLocked.linkState(unlock);
        unlock.linkState(open);
        open.linkState(close);
        close.linkState(idleUnlocked);
        idleUnlocked.linkState(open);
    }

    public Collectable.Type getDrop() {
        int index = Utils.isOccurring(DROPPABLE_COLLECTABLE_PROB);

        if (index == 3) {
            if (onePowerUp) {
                index = -1;
            } else {
                onePowerUp = true;
            }
        }
        if (index == -1) {
            return getDrop();
        }

        return DROPPABLE_COLLECTABLE[index];
    }

    public int getSpawnQuantity() {
        return Utils.random.nextInt(minDrop, maxDrop + 1);
    }

    public boolean isFirstOpen() {
        if (isFirstOpen) {
            isFirstOpen = false;
            return true;
        }
        return false;
    }

    public synchronized GlobalState getState(CommonState commonEnumStates) {
        State chestState = (State) commonEnumStates;
        return switch (chestState) {
            case IDLE_LOCKED -> idleLocked;
            case IDLE_UNLOCKED -> idleUnlocked;
            case OPEN -> open;
            case UNLOCK -> unlock;
            case CLOSE -> close;
        };
    }

    public enum State implements CommonState {
        IDLE_LOCKED, IDLE_UNLOCKED, OPEN, UNLOCK, CLOSE
    }
}
