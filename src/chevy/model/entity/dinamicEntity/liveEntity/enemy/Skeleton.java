package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.utilz.Vector2;

public class Skeleton extends Enemy {
    public Skeleton(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.SKELETON);
    }
}
