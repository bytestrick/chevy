package chevy.model.entity;

import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.model.entity.stateMachine.StateMachine;
import chevy.utils.Log;
import chevy.utils.Vector2;

import java.util.Random;
import java.util.UUID;

public abstract class Entity {
    protected final Vector2<Integer> position;
    protected final StateMachine stateMachine = new StateMachine();
    private final UUID ID = UUID.randomUUID();
    private final Type type;
    protected int maxDamage;
    protected int minDamage;
    protected boolean safeToCross;
    protected boolean crossable;
    protected int layer;
    private boolean toDraw;

    public Entity(Vector2<Integer> initPosition, Type type) {
        this.position = initPosition;
        this.type = type;

        this.crossable = false;
        this.safeToCross = true;

        this.layer = 0;
        this.toDraw = false;
    }

    public boolean isToDraw() {
        return toDraw;
    }

    public void setToDraw(boolean toDraw) {
        this.toDraw = toDraw;
    }

    public synchronized int getDamage() {
        Random random = new Random();
        return random.nextInt(minDamage, maxDamage + 1);
    }

    public EntityCommonEnumTypes getSpecificType() { return type; }

    public EntityCommonEnumTypes getGenericType() { return null; }

    public final int getRow() { return position.first; }

    public final int getCol() { return position.second; }

    public boolean isCrossable() { return crossable; }

    public boolean isSafeToCross() {
        if (crossable) return safeToCross;

        return false;
    }

    public int getLayer() {
        return Math.abs(this.layer);
    }

    public State getState(CommonStates commonEnumStates) {
        Log.warn("La funzione getState() deve essere ridefinita opportunamente nelle classi figlie");
        return null;
    }

    public boolean changeState(CommonStates state) {
        return stateMachine.changeState(state);
    }

    public boolean canChange(CommonStates state) {
        return stateMachine.canChange(state);
    }

    public boolean checkAndChangeState(CommonStates state) {
        return stateMachine.checkAndChangeState(state);
    }

    public boolean changeToPreviousState() {
        return stateMachine.changeToPreviousState();
    }

    public CommonStates getCurrentState() {
        return stateMachine.getCurrentState().getStateEnum();
    }

    public CommonStates getPreviousEnumState() {
        return stateMachine.getPreviousState().getStateEnum();
    }

    @Override
    public String toString() {
        return "ENTITY";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return ID.equals(entity.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    public enum Type implements EntityCommonEnumTypes {
        DYNAMIC, ENVIRONMENT, POWER_UP
    }
}