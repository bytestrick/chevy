package chevy.model.entity.dynamicEntity.liveEntity.player;

import chevy.utils.Vector2;

public final class Knight extends Player {
    public Knight(Vector2<Integer> initPosition) {
        super(initPosition, Type.KNIGHT, .2f, 10, 5, 7, 4, .5f, .6f);
        stateMachine.setName("Knight");
        hit.linkVertex(fall);
    }
}