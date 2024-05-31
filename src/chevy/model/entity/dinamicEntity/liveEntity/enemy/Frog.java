package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.utilz.Vector2;

public class Frog extends Enemy {
    public Frog(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.FROG);
    }
}
