package chevy.model.dinamicEntity.player;

import chevy.model.dinamicEntity.DynamicEntity;
import chevy.utilz.Vector2;

public abstract class Player extends DynamicEntity {
    PlayerTypes type;
    public Player(Vector2<Integer> initVelocity, PlayerTypes type) {
        super(initVelocity);
        this.type = type;
    }
}
