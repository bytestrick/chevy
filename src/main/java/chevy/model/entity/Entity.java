package chevy.model.entity;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.model.entity.stateMachine.StateMachine;
import chevy.utils.Log;
import chevy.utils.Utils;
import chevy.utils.Vector2;

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
    protected int drawLayer;
    protected boolean mustBeUpdate = false;
    private boolean toDraw;

    public Entity(Vector2<Integer> initPosition, Type type) {
        this.position = initPosition;
        this.type = type;

        this.crossable = false;
        this.safeToCross = true;

        this.drawLayer = 0;
        this.toDraw = false;
    }

    public boolean toDraw() { return toDraw; }

    public void setToDraw(boolean toDraw) { this.toDraw = toDraw; }

    public synchronized int getDamage() { return Utils.random.nextInt(minDamage, maxDamage + 1); }

    public void changeMinDamage(int minDamage) { this.minDamage = minDamage; }

    public void changeMaxDamage(int maxDamage) { this.maxDamage = maxDamage; }

    public int getMaxDamage() { return maxDamage; }

    public int getMinDamage() { return minDamage; }

    public CommonEntityType getSpecificType() { return type; }

    public CommonEntityType getGenericType() { return null; }

    public final int getRow() { return position.first; }

    public final int getCol() { return position.second; }

    public final Vector2<Integer> getPosition() { return position;}

    public boolean isCrossable() { return crossable; }

    public boolean isSafeToCross() {
        if (crossable) {
            return safeToCross;
        }
        return false;
    }

    public int getDrawLayer() { return Math.abs(this.drawLayer); }

    public Vertex getState(CommonState commonEnumStates) {
        Log.warn("La funzione getState() deve essere ridefinita opportunamente nelle classi figlie");
        return null;
    }

    public boolean changeState(CommonState state) { return stateMachine.changeState(state); }

    public boolean canChange(CommonState state) { return stateMachine.canChange(state); }

    public boolean checkAndChangeState(CommonState state) { return stateMachine.checkAndChangeState(state); }

    public boolean changeToPreviousState() { return stateMachine.changeToPreviousState(); }

    public CommonState getCurrentState() {
        Vertex currentVertex = stateMachine.getCurrentState();
        if (currentVertex == null) {
            return null;
        }
        return currentVertex.getState();
    }

    public CommonState getPreviousState() {
        Vertex previousVertex = stateMachine.getPreviousState();
        if (previousVertex == null) {
            return null;
        }
        return previousVertex.getState();
    }

    public boolean canRemoveToUpdate() { return !mustBeUpdate; }

    public void removeFromUpdate() { mustBeUpdate = false; }

    @Override
    public String toString() { return "ENTITY"; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity entity = (Entity) o;
        return ID.equals(entity.ID);
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    public enum Type implements CommonEntityType {
        DYNAMIC, ENVIRONMENT, COLLECTABLE
    }
}