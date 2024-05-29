package chevy.model.entity.dinamicEntity.enemy;

import chevy.utilz.Vector2;

public class Wizard extends Enemy {
    public Wizard(Vector2<Integer> initVelocity) {
        super(initVelocity, EnemyTypes.WIZARD);
    }
}
