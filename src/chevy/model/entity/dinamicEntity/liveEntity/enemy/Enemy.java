package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.utilz.Vector2;

public abstract class Enemy extends LiveEntity {
    private final EnemyTypes type;
    protected int updateEverySecond;
    private int nUpdate;


    public Enemy(Vector2<Integer> initPosition, EnemyTypes type) {
        super(initPosition, LiveEntityTypes.ENEMY);
        this.type = type;
        this.updateEverySecond = 3;
        this.nUpdate = 0;
    }


    public int getUpdateEverySecond() {
        return updateEverySecond;
    }

    public int getCurrentNUpdate() {
        return nUpdate;
    }

    public void incrementNUpdate() {
        ++nUpdate;
    }

    public void resetNUpdate() {
        nUpdate = 0;
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
