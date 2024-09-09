package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Utils;

import java.awt.Point;

public final class Chest extends Environment {
    /** Oggetti collezionabili che i nemici possono rilasciare */
    private static final Collectable.Type[] DROPPABLE_COLLECTABLE = {
            Collectable.Type.COIN, Collectable.Type.KEY, Collectable.Type.HEALTH,
            Collectable.Type.POWER_UP};
    /** Probabilità di rilascio degli oggetti collezionabili */
    private static final int[] DROPPABLE_COLLECTABLE_PROB = {100, 15, 15, 35};
    private final Vertex idleLocked = new Vertex(State.IDLE_LOCKED);
    private final Vertex idleUnlocked = new Vertex(State.IDLE_UNLOCKED);
    private final Vertex open = new Vertex(State.OPEN, 0.8f);
    private final Vertex unlock = new Vertex(State.UNLOCK, 0.3f);
    private final Vertex close = new Vertex(State.CLOSE, 0.8f);
    private boolean onePowerUp;
    private boolean isFirstOpen = true;

    public Chest(Point initVelocity) {
        super(initVelocity, Type.CHEST);
        shouldUpdate = true;
        initStateMachine();
    }

    public static int getSpawnQuantity() {
        final int minDrop = 3, maxDrop = 6;
        return Utils.random.nextInt(minDrop, maxDrop + 1);
    }

    private void initStateMachine() {
        stateMachine.setName("Chest");
        stateMachine.setInitialState(idleLocked);

        idleLocked.linkVertex(unlock);
        unlock.linkVertex(open);
        open.linkVertex(close);
        close.linkVertex(idleUnlocked);
        idleUnlocked.linkVertex(open);
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

    public boolean isFirstOpen() {
        if (isFirstOpen) {
            isFirstOpen = false;
            return true;
        }
        return false;
    }

    public synchronized Vertex getState(EntityState state) {
        State chestState = (State) state;
        return switch (chestState) {
            case IDLE_LOCKED -> idleLocked;
            case IDLE_UNLOCKED -> idleUnlocked;
            case OPEN -> open;
            case UNLOCK -> unlock;
            case CLOSE -> close;
        };
    }

    public enum State implements EntityState {IDLE_LOCKED, IDLE_UNLOCKED, OPEN, UNLOCK, CLOSE}
}