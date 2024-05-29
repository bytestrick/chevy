package chevy.model.entity.dinamicEntity.enemy;

import chevy.utilz.Vector2;

public class Skeleton extends Enemy {
    public Skeleton(Vector2<Integer> initVelocity) {
        super(initVelocity, EnemyTypes.SKELETON);
    }
}
