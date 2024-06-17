package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.utilz.Vector2;

import java.util.Random;

public abstract class Player extends LiveEntity {
    private final PlayerTypes type;


    public Player(Vector2<Integer> initPosition, PlayerTypes type) {
        super(initPosition, LiveEntityTypes.PLAYER);
        this.type = type;
    }


    @Override
    public PlayerTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
