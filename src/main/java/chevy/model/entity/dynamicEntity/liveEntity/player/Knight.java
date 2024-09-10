package chevy.model.entity.dynamicEntity.liveEntity.player;

import java.awt.Point;

public final class Knight extends Player {
    public Knight(Point initPosition) {
        super(initPosition, Type.KNIGHT, .2f, 10, 5, 7, 4, .5f, .6f);
        stateMachine.setName("Knight");
        hit.linkVertex(fall);
    }
}
