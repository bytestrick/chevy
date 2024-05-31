package chevy.model.entity.dinamicEntity.liveEntity.player;

import chevy.utilz.Vector2;

public class Archer extends Player {
    public Archer(Vector2<Integer> initPosition) {
        super(initPosition, PlayerTypes.ARCHER);
    }
}
