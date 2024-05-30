package chevy.model.entity.dinamicEntity.enemy;

import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.utilz.Vector2;

public abstract class Enemy extends DynamicEntity {
    private final EnemyTypes type;
    protected int health;
    protected int maxDamage;
    protected int minDamage;
    private boolean alive;


    public Enemy(Vector2<Integer> initVelocity, EnemyTypes type) {
        super(initVelocity, DynamicEntityTypes.ENEMY);
        this.type = type;
        this.alive = true;
    }

    public void changeHealth(int value) {
        this.health += value;
        System.out.println("vita corrente: " + health);
        if (health <= 0) {
            alive = false;
            this.health = 0;
        }
    }

    public void changeShield(int value) {}


    public boolean isAlive() {
        return alive;
    }

    @Override
    public EntityTypes getSpecificType() { return type; }

    @Override
    public EntityTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() {
        return type.toString();
    }
}
