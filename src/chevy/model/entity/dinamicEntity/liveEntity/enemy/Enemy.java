package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.utilz.Vector2;

public abstract class Enemy extends LiveEntity {
    private final EnemyTypes type;


    public Enemy(Vector2<Integer> initPosition, EnemyTypes type) {
        super(initPosition, LiveEntityTypes.ENEMY);
        this.type = type;
    }


    @Override
    public EntityTypes getSpecificType() { return type; }

    @Override
    public EntityTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() {
        return type.toString();
    }
}
