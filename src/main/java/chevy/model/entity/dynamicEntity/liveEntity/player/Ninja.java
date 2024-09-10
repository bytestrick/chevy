package chevy.model.entity.dynamicEntity.liveEntity.player;

import java.awt.Point;

public final class Ninja extends Player {
    public Ninja(Point initPosition) {
        super(initPosition, Type.NINJA, .1f, 6, 2, 5, 4, .3f, .3f);
        stateMachine.setName("Ninja");
        fall.linkVertex(hit);
    }
}