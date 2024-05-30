package chevy.model.entity.dinamicEntity.player;

import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.model.entity.EntityTypes;
import chevy.utilz.Vector2;

import java.util.Random;

public abstract class Player extends DynamicEntity {
    private final PlayerTypes type;
    protected int health;
    protected int shield;
    protected int maxDamage;
    protected int minDamage;
    private boolean alive;


    public Player(Vector2<Integer> initVelocity, PlayerTypes type) {
        super(initVelocity, DynamicEntityTypes.PLAYER);
        this.type = type;
        this.alive = true;
    }

    public void changeHealth(int value) {
        this.health -= value;
        if (health <= 0) {
            alive = false;
            this.health = 0;
        }
    }

    public void changeShield(int value) {
        this.shield += value;
    }

    public int getDamage() {
        Random random = new Random();
        return random.nextInt(minDamage, maxDamage);
    }

    @Override
    public PlayerTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
