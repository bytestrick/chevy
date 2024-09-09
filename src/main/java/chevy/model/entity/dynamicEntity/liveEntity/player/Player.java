package chevy.model.entity.dynamicEntity.liveEntity.player;

import chevy.model.entity.EntityType;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Log;

import java.awt.Point;
import java.util.EnumMap;
import java.util.Map;

public abstract class Player extends LiveEntity {
    final Vertex hit;
    final Vertex fall;
    private final Vertex idle, move, attack, dead, sludge, glide;
    private final Type type;
    private final float speed;
    private final Map<PowerUp.Type, PowerUp> ownedPowerUp = new EnumMap<>(PowerUp.Type.class);

    Player(Point initPosition, Type type, float speed, int health, int shield,
           int maxDamage, int minDamage, float attackDuration,
           float deadDuration) {
        super(initPosition, LiveEntity.Type.PLAYER);
        this.type = type;

        this.speed = speed;
        this.health = health;
        currentHealth = health;
        this.shield = shield;
        currentShield = shield;
        this.maxDamage = maxDamage;
        this.minDamage = minDamage;

        idle = new Vertex(State.IDLE);
        move = new Vertex(State.MOVE, speed, true);
        attack = new Vertex(State.ATTACK, attackDuration, true);
        hit = new Vertex(State.HIT, (float) 0.2);
        dead = new Vertex(State.DEAD, deadDuration);
        sludge = new Vertex(State.SLUDGE, speed);
        glide = new Vertex(State.GLIDE, speed, true);
        fall = new Vertex(State.FALL);

        drawLayer = 2;
        initStateMachine();
    }

    public boolean acquirePowerUp(PowerUp.Type powerUpType, PowerUp powerUp) {
        if (!ownedPowerUp.containsKey(powerUpType)) {
            ownedPowerUp.put(powerUpType, powerUp);
            Log.info(this + " acquires the power up " + powerUpType);
            return true;
        }
        return false;
    }

    public int[] getStats() {
        return new int[]{health * 10, shield * 10, maxDamage * 10, 100 - (int) (speed * 100)};
    }

    public PowerUp getOwnedPowerUp(PowerUp.Type powerUpType) {return ownedPowerUp.get(powerUpType);}

    @Override
    public Type getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    @Override
    public String toString() {return type.toString();}

    private void initStateMachine() {
        idle.linkVertex(move);
        idle.linkVertex(attack);
        idle.linkVertex(hit);
        idle.linkVertex(fall);
        hit.linkVertex(idle);
        hit.linkVertex(dead);
        hit.linkVertex(move);
        move.linkVertex(glide);
        move.linkVertex(hit);
        move.linkVertex(fall);
        move.linkVertex(sludge);
        move.linkVertex(idle);
        move.linkVertex(attack);
        attack.linkVertex(idle);
        attack.linkVertex(move);
        attack.linkVertex(hit);
        glide.linkVertex(idle);
        glide.linkVertex(fall);
        glide.linkVertex(hit);
        glide.linkVertex(sludge);
        sludge.linkVertex(idle);
        fall.linkVertex(idle);
        fall.linkVertex(dead);
        stateMachine.setInitialState(idle);
    }

    @Override
    public synchronized Vertex getState(EntityState state) {
        return switch ((State) state) {
            case IDLE -> idle;
            case ATTACK -> attack;
            case MOVE -> move;
            case DEAD -> dead;
            case HIT -> hit;
            case SLUDGE -> sludge;
            case FALL -> fall;
            case GLIDE -> glide;
        };
    }

    public enum State implements EntityState {IDLE, ATTACK, MOVE, DEAD, HIT, SLUDGE, FALL, GLIDE}

    public enum Type implements EntityType {KNIGHT, ARCHER, NINJA}
}