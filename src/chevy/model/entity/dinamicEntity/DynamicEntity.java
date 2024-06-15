package chevy.model.entity.dinamicEntity;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityCommonTypes;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.model.entity.dinamicEntity.stateMachine.StateMachine;
import chevy.model.entity.staticEntity.StaticEntityTypes;
import chevy.utilz.Vector2;

public abstract class DynamicEntity extends Entity {
    private final DynamicEntityTypes type;
    protected final StateMachine stateMachine = new StateMachine();


    public DynamicEntity(Vector2<Integer> initPosition, DynamicEntityTypes type) {
        super(initPosition, StaticEntityTypes.DYNAMIC);
        this.type = type;
    }


    public void changePosition(Vector2<Integer> velocity) {
        this.position.change(velocity);
    }

    public boolean changeState(CommonEnumStates state) {
        return stateMachine.changeState(state);
    }

    public boolean canChange(CommonEnumStates state) {
        return stateMachine.canChange(state);
    }

    public boolean checkAndChangeState(CommonEnumStates state) {
        return stateMachine.checkAndChangeState(state);
    }

    public boolean changeToPreviousState() {
        return stateMachine.changeToPreviousState();
    }

    public synchronized CommonEnumStates getCurrentEumState() {
        return stateMachine.getCurrentState().getStateEnum();
    }

    public synchronized CommonEnumStates getPreviousEumState() {
        State state = stateMachine.getPreviousState();
        if (state == null)
            return null;
        return state.getStateEnum();
    }

    public State getState(CommonEnumStates commonEnumStates) {
        return null;
    }


    @Override
    public EntityCommonTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return "DYNAMIC ENTITY";
    }
}