package chevy.model.dinamicEntity.player;

import chevy.model.dinamicEntity.Directions;
import chevy.model.dinamicEntity.DynamicEntity;
import chevy.model.stateMachine.State;
import chevy.model.stateMachine.StateMachine;
import chevy.utilz.Vector2;

public abstract class Player extends DynamicEntity {
    PlayerTypes type;
    protected final StateMachine stateMachine = new StateMachine();

    public Player(Vector2<Integer> initVelocity, PlayerTypes type) {
        super(initVelocity);
        this.type = type;
    }
}
