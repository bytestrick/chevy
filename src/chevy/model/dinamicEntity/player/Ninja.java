package chevy.model.dinamicEntity.player;

import chevy.utilz.Vector2;

public class Ninja extends Player {
    public Ninja(Vector2<Integer> initVelocity) {
        super(initVelocity, PlayerTypes.NINJA);
    }
}