package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.utilz.Vector2;

public class Wizard extends Enemy {
    public Wizard(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.WIZARD);
    }
}
