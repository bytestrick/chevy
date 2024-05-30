package chevy.model.entity.dinamicEntity.enemy;

import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.utilz.Vector2;

public abstract class Enemy extends DynamicEntity {
    private final EnemyTypes type;
    public Enemy(Vector2<Integer> initVelocity, EnemyTypes type) {
        super(initVelocity, DynamicEntityTypes.ENEMY);
        this.type = type;
    }


    @Override
    public EntityTypes getSpecificType() { return type; }

    @Override
    public EntityTypes getGenericType() { return super.getSpecificType(); }
}
