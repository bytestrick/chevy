package chevy.model.entity.dinamicEntity;

import chevy.model.entity.Entity;
import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.model.entity.dinamicEntity.stateMachine.StateMachine;
import chevy.utilz.Vector2;

public abstract class DynamicEntity extends Entity {
    protected final StateMachine stateMachine = new StateMachine();

    public enum Type implements EntityCommonEnumTypes {
        LIVE_ENTITY,
        PROJECTILE;


    }
    private final Type type;


    public DynamicEntity(Vector2<Integer> initPosition, Type type) {
        super(initPosition, Entity.Type.DYNAMIC);
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

    public CommonEnumStates getCurrentEumState() {
        return stateMachine.getCurrentState().getStateEnum();
    }

    public CommonEnumStates getPreviousEnumState() {
        return stateMachine.getPreviousState().getStateEnum();
    }

    public synchronized CommonEnumStates getPreviousEumState() {
        State state = stateMachine.getPreviousState();
        if (state == null)
            return null;
        return state.getStateEnum();
    }

    public State getState(CommonEnumStates commonEnumStates) {
        System.out.println("ATTENZIONE: La funzione getState deve essere ridefinita opportunamente nelle classi figlie");
        return null;
    }


    @Override
    public EntityCommonEnumTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonEnumTypes getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return "DYNAMIC ENTITY";
    }
}