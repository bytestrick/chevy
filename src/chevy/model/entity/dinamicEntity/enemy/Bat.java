package chevy.model.entity.dinamicEntity.enemy;

import chevy.utilz.Vector2;

public class Bat extends Enemy{
    public Bat(Vector2<Integer> initVelocity) {
        super(initVelocity, EnemyTypes.BAT);
    }
}
