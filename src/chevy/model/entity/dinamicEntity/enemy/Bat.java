package chevy.model.entity.dinamicEntity.enemy;

import chevy.utilz.Vector2;

public class Bat extends Enemy {
    public Bat(Vector2<Integer> initVelocity) {
        super(initVelocity, EnemyTypes.BAT);
        this.health = 5;
        this.maxDamage = 2;
        this.minDamage = 1;
    }
}
