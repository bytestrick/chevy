package chevy.model.entity.dinamicEntity;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.model.entity.dinamicEntity.stateMachine.StateEnum;
import chevy.model.entity.dinamicEntity.stateMachine.StateMachine;
import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.utilz.Vector2;

public abstract class DynamicEntity extends Entity {
    private final DynamicEntityTypes type;
    protected final StateMachine stateMachine = new StateMachine();


    public DynamicEntity(Vector2<Integer> initVelocity, DynamicEntityTypes type) {
        super(initVelocity, StaticEntityTypes.DYNAMIC);
        this.type = type;
    }


    public void changePosition(Vector2<Integer> velocity) {
        this.position.change(velocity);
    }

    public boolean changeState(StateEnum state) {
        return stateMachine.changeState(state);
    }


    @Override
    public EntityTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return "DYNAMIC ENTITY";
    }
}