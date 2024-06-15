package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.model.entity.EntityCommonTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.utilz.Vector2;

public abstract class Player extends LiveEntity {
    public enum States implements CommonEnumStates {
        IDLE,
        ATTACK,
        MOVE,
        DEAD,
        HIT,
        SLUDGE,
        FALL,
        GLIDE
    }
    private final PlayerTypes type;


    public Player(Vector2<Integer> initPosition, PlayerTypes type) {
        super(initPosition, LiveEntityTypes.PLAYER);
        this.type = type;

        this.layer = 2;
    }


    @Override
    public PlayerTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
