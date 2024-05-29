package chevy.model.entity.dinamicEntity.player;

import chevy.utilz.Vector2;

public class Archer extends Player {
    public Archer(Vector2<Integer> initVelocity) {
        super(initVelocity, PlayerTypes.ARCHER);
    }
}
