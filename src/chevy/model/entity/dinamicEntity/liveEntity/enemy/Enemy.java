package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.utils.Vector2;

public abstract class Enemy extends LiveEntity {
    public enum Type implements EntityCommonEnumTypes {
        WRAITH,
        ZOMBIE,
        WIZARD,
        SKELETON,
        SLIME,
        BIG_SLIME,
        BEETLE;
    }
    private final Type type;


    public Enemy(Vector2<Integer> initPosition, Type type) {
        super(initPosition, LiveEntity.Type.ENEMY);
        this.type = type;

        this.layer = 2;
    }



    @Override
    public EntityCommonEnumTypes getSpecificType() { return type; }

    @Override
    public EntityCommonEnumTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() {
        return type.toString();
    }
}
