package chevy.model.dinamicEntity.enemy;

import chevy.model.dinamicEntity.DynamicEntity;
import chevy.utilz.Vector2;

public abstract class Enemy extends DynamicEntity {
    EnemyTypes type;
    public Enemy(Vector2<Integer> initVelocity, EnemyTypes type) {
        super(initVelocity);
        this.type = type;
    }
}
