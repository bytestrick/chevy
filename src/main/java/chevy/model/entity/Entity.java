package chevy.model.entity;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.StateMachine;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Log;
import chevy.utils.Utils;
import chevy.utils.Vector2;

import java.util.UUID;

public abstract class Entity {
    protected final Vector2<Integer> position;
    protected final StateMachine stateMachine = new StateMachine();
    private final UUID uuid = UUID.randomUUID();
    private final Type type;
    protected int maxDamage;
    protected int minDamage;
    protected boolean safeToCross;
    protected boolean crossable;
    protected int drawLayer;
    protected boolean shouldUpdate;
    protected Direction direction = Direction.getRandom();
    private boolean toDraw;

    public Entity(Vector2<Integer> initPosition, Type type) {
        position = initPosition;
        this.type = type;
        crossable = false;
        safeToCross = true;
        drawLayer = 0;
        toDraw = false;
    }

    public Direction getDirection() {return direction;}

    public void setDirection(Direction direction) {this.direction = direction;}

    public void activated() {safeToCross = false;}

    public void disabled() {safeToCross = true;}

    public boolean toNotDraw() {return !toDraw;}

    public void setToDraw(boolean toDraw) {this.toDraw = toDraw;}

    public synchronized int getDamage() {return Utils.random.nextInt(minDamage, maxDamage + 1);}

    public void changeMinDamage(int minDamage) {this.minDamage = minDamage;}

    public void changeMaxDamage(int maxDamage) {this.maxDamage = maxDamage;}

    public int getMaxDamage() {return maxDamage;}

    public int getMinDamage() {return minDamage;}

    public EntityType getType() {return type;}

    public EntityType getGenericType() {return null;}

    public final int getRow() {return position.first;}

    public final int getCol() {return position.second;}

    public final Vector2<Integer> getPosition() {return position;}

    public boolean isCrossable() {return crossable;}

    public boolean isSafeToCross() {
        if (crossable) {
            return safeToCross;
        }
        return false;
    }

    public int getDrawLayer() {return Math.abs(drawLayer);}

    public Vertex getState(EntityState state) {
        Log.warn("La funzione getState() deve essere ridefinita opportunamente nelle classi " +
                "figlie");
        return null;
    }

    public boolean changeState(EntityState state) {return stateMachine.changeState(state);}

    public boolean canChange(EntityState state) {return stateMachine.canChange(state);}

    public boolean checkAndChangeState(EntityState state) {return stateMachine.checkAndChangeState(state);}

    public EntityState getState() {
        Vertex currentVertex = stateMachine.getCurrentState();
        if (currentVertex == null) {
            return null;
        }
        return currentVertex.getState();
    }

    public boolean canRemoveToUpdate() {return !shouldUpdate;}

    public void removeFromUpdate() {shouldUpdate = false;}

    @Override
    public String toString() {return "ENTITY";}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entity entity = (Entity) o;
        return uuid.equals(entity.uuid);
    }

    @Override
    public int hashCode() {return uuid.hashCode();}

    public enum Type implements EntityType {DYNAMIC, ENVIRONMENT, COLLECTABLE}
}