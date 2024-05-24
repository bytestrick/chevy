package chevy.model.dinamicEntity.player;

import chevy.model.dinamicEntity.DynamicEntity;
import chevy.utilz.Vector2;

public class Knight extends Player {
    public Knight(Vector2<Integer> initVelocity) {
        super(initVelocity, PlayerTypes.KNIGHT);
    }
}
