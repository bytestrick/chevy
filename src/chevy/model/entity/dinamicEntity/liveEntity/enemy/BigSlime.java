package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.utilz.Vector2;

public class BigSlime extends Enemy {
    public BigSlime(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.BIG_SLIME);
    }
}
