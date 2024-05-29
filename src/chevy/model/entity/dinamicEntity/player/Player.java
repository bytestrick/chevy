package chevy.model.entity.dinamicEntity.player;

import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.model.entity.dinamicEntity.stateMachine.StateMachine;
import chevy.model.entity.EntityTypes;
import chevy.utilz.Vector2;

public abstract class Player extends DynamicEntity {
    private final PlayerTypes type;
    protected final StateMachine stateMachine = new StateMachine();


    public Player(Vector2<Integer> initVelocity, PlayerTypes type) {
        super(initVelocity, DynamicEntityTypes.PLAYER);
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
