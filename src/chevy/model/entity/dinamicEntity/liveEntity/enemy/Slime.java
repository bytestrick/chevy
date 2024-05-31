package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.utilz.Vector2;

public class Slime extends Enemy {
    public Slime(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.SLIME);
    }
}
