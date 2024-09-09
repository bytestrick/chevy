package chevy.model.entity;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.StateMachine;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Log;
import chevy.utils.Utils;

import java.awt.Point;
import java.util.UUID;

public abstract class Entity {
    protected final Point position;
    protected final StateMachine stateMachine = new StateMachine();
    private final UUID uuid = UUID.randomUUID();
    private final Type type;
    protected int maxDamage;
    protected int minDamage;
    protected boolean safeToCross = true;
    protected boolean crossable;
    protected int drawLayer;
    protected boolean shouldUpdate;
    private Direction direction = Direction.getRandom();
    private boolean shouldDraw;

    public Entity(Point initialPosition, Type type) {
        position = initialPosition;
        this.type = type;
    }

    public EntityState getState() {
        Vertex currentVertex = stateMachine.getCurrentState();
        if (currentVertex == null) {
            return null;
        }
        return currentVertex.getState();
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

    public Direction getDirection() {return direction;}

    public void setDirection(Direction direction) {this.direction = direction;}

    public boolean shouldNotDraw() {return !shouldDraw;}

    public void setShouldDraw(boolean shouldDraw) {this.shouldDraw = shouldDraw;}

    public synchronized int getDamage() {return Utils.random.nextInt(minDamage, maxDamage + 1);}

    public void setDamage(int minDamage, int maxDamage) {
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public int getMaxDamage() {return maxDamage;}

    public int getMinDamage() {return minDamage;}

    public EntityType getType() {return type;}

    public EntityType getGenericType() {return null;}

    public final int getRow() {return position.y;}

    public final int getCol() {return position.x;}

    public final Point getPosition() {return new Point(position);}

    public boolean isCrossable() {return crossable;}

    public boolean shouldNotUpdate() {return !shouldUpdate;}

    public void removeFromUpdate() {shouldUpdate = false;}

    public boolean isSafeToCross() {
        if (crossable) {
            return safeToCross;
        }
        return false;
    }

    public void setSafeToCross(boolean safeToCross) {this.safeToCross = safeToCross;}

    @Override
    public int hashCode() {return uuid.hashCode();}

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

    public enum Type implements EntityType {DYNAMIC, ENVIRONMENT, COLLECTABLE}
}