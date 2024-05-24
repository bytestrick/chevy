package chevy.model.dinamicEntity.enemy;

import chevy.utilz.Vector2;

public class Slime extends Enemy {
    public Slime(Vector2<Integer> initVelocity) {
        super(initVelocity, EnemyTypes.SLIME);
    }
}
